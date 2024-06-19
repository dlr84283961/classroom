package cn.ninghan.front;

import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"cn.ninghan.common","cn.ninghan.front"})
@EnableTransactionManagement
public class FrontApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class,args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(FrontApplication.class);
    }

    @Bean("shardedJedisPool")
    public ShardedJedisPool shardedJedisPool(){
        JedisShardInfo jedisShardInfo = new JedisShardInfo("192.168.141.133","6379");
        jedisShardInfo.setPassword("123456");
        List<JedisShardInfo> jedisShardInfoList = Lists.newArrayList(jedisShardInfo);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config,jedisShardInfoList);
        return shardedJedisPool;
    }
}
