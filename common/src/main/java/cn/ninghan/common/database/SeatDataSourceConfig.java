package cn.ninghan.common.database;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "cn.ninghan.common.seatDao",sqlSessionTemplateRef = "seatSqlSessionTemplate")
public class SeatDataSourceConfig {
    @Bean(DataSourceConstants.CLASSROOM_SEAT_DB_1)
    @ConfigurationProperties(prefix = "spring.datasource-seat1")
    public DataSource seatDataSource1(){
        return DataSourceBuilder.create().build();
    }
    @Bean(DataSourceConstants.CLASSROOM_SEAT_DB_2)
    @ConfigurationProperties(prefix = "spring.datasource-seat2")
    public DataSource seatDataSource2(){
        return DataSourceBuilder.create().build();
    }

    @Bean("seatShardingDatasource")
    public DataSource seatShardingDatasource(@Qualifier(DataSourceConstants.CLASSROOM_SEAT_DB_1)DataSource dataSource1,
                                             @Qualifier(DataSourceConstants.CLASSROOM_SEAT_DB_2)DataSource dataSource2) throws SQLException {
        Map<String,DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceConstants.CLASSROOM_SEAT_DB_1,dataSource1);
        dataSourceMap.put(DataSourceConstants.CLASSROOM_SEAT_DB_2,dataSource2);
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
        tableRuleConfiguration.setLogicTable("classroom_seat");
        tableRuleConfiguration.setActualDataNodes(DataSourceConstants.CLASSROOM_SEAT_DB_1+".classroom_seat_1,"+
                DataSourceConstants.CLASSROOM_SEAT_DB_2+".classroom_seat_2");
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration("classroom_number_id",new DataBaseShardingAlgorithm()));
        tableRuleConfiguration.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("classroom_number_id",new TableShardingAlgorithm()));
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(tableRuleConfiguration);
        return ShardingDataSourceFactory.createDataSource(dataSourceMap,shardingRuleConfiguration,new HashMap<>(),new Properties());
    }

    @Bean("seatDataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("seatShardingDatasource")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean("seatSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("seatShardingDatasource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:seatMappers/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }
    @Bean("seatSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("seatSqlSessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    private class DataBaseShardingAlgorithm implements PreciseShardingAlgorithm<Integer>{
        private final static String DB_pre = "classroomSeatDB";
        private String getRealName(int value){
            int i = value%2;
            if(i==0){
                return DB_pre+"1";
            }else{
                return DB_pre+"2";
            }
        }
        @Override
        public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
            String realDBName = getRealName(preciseShardingValue.getValue());
            if(collection.contains(realDBName)){
                return realDBName;
            }
            throw new IllegalArgumentException();
        }
    }

    private class TableShardingAlgorithm implements PreciseShardingAlgorithm<Integer>{
        private final static String TABLE_pre = "classroom_seat_";
        private String getRealName(int value){
            int i = value%2;
            if(i==0){
                return TABLE_pre+"1";
            }else{
                return TABLE_pre+"2";
            }
        }
        @Override
        public String doSharding(Collection<String> collection, PreciseShardingValue<Integer> preciseShardingValue) {
            String realTableName = getRealName(preciseShardingValue.getValue());
            if(collection.contains(realTableName)){
                return realTableName;
            }
            throw new IllegalArgumentException();
        }
    }
}
