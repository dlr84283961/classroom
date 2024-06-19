package cn.ninghan.common.database;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shardingsphere.api.algorithm.masterslave.RoundRobinMasterSlaveLoadBalanceAlgorithm;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "cn.ninghan.common.dao",sqlSessionTemplateRef = "selSessionTemplate")
public class BasicDataSourceConfig {
    @Primary
    @Bean(name = DataSourceConstants.MASTER_DB)
    @ConfigurationProperties(prefix = "spring.datasource-master")
    public DataSource masterDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = DataSourceConstants.SLAVE_DB)
    @ConfigurationProperties(prefix = "spring.datasource-slave")
    public DataSource slaveDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "masterSlaveDataSource")
    public DataSource masterSlaveDataSource(@Qualifier(DataSourceConstants.MASTER_DB)DataSource masterDataSource,
                                            @Qualifier(DataSourceConstants.SLAVE_DB)DataSource slaveDataSource) throws SQLException {
        Map<String ,DataSource> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceConstants.MASTER_DB,masterDataSource);
        dataSourceMap.put(DataSourceConstants.SLAVE_DB,slaveDataSource);
        MasterSlaveRuleConfiguration configuration = new MasterSlaveRuleConfiguration("db_master_slave",
                DataSourceConstants.MASTER_DB, Lists.newArrayList(DataSourceConstants.SLAVE_DB),new RoundRobinMasterSlaveLoadBalanceAlgorithm());
        return MasterSlaveDataSourceFactory.createDataSource(dataSourceMap,configuration, Maps.newHashMap(),new Properties());
    }

    @Primary
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("masterSlaveDataSource")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("masterSlaveDataSource")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Bean("selSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("SqlSessionFactory")SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
