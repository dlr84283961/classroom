package cn.ninghan.canal;

import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.ninghan.common","cn.ninghan.canal"})
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class CanalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanalApplication.class,args);
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
