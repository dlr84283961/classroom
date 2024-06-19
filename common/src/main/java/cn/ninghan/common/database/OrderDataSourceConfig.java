package cn.ninghan.common.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

//@Configuration
@MapperScan(basePackages = "cn.ninghan.common.orderDao",sqlSessionTemplateRef = "orderSqlSessionTemplate")
public class OrderDataSourceConfig {
    @Bean(DataSourceConstants.CLASSROOM_ORDER_DB)
    @ConfigurationProperties(prefix = "spring.datasource-order")
    public DataSource orderDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("orderDataSourceTransactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier(DataSourceConstants.CLASSROOM_ORDER_DB)DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean("orderSqlSessionFactory")
    public SqlSessionFactory orderSqlSessionFactory(@Qualifier(DataSourceConstants.CLASSROOM_ORDER_DB)DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:orderMappers/*.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean("orderSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("orderSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
