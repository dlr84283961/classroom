package cn.ninghan.canal.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class RedisUtil {
    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;
    public static String CLASSROOM_NUMBER_KEY = "classroomNumber";
    public static String LOCK_KEY_CLASSROOM = "lock_key_classroom";
    private ShardedJedis getJedis(){
        return shardedJedisPool.getResource();
    }

    private void safeClose(ShardedJedis shardedJedis){
        try {
            if(shardedJedis!=null){
                shardedJedis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis关闭失败");
        }
    }

    public void hSet(String key,String field,String value){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.hset(key,field,value);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
           safeClose(shardedJedis);
        }
    }

    public String hGet(String key,String field){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            return shardedJedis.hget(key,field);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            safeClose(shardedJedis);
        }
        return "";
    }

    public void setAddOne(String key,Integer value){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.sadd(key,value.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            safeClose(shardedJedis);
        }
    }

    public void setDeleteOne(String key,Integer value){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.srem(key,value.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            safeClose(shardedJedis);
        }
    }
    public void setDeleteMembers(String key,List<Integer> members){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.srem(key,(String[]) members.toArray());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            safeClose(shardedJedis);
        }
    }

    public boolean tryLock(String key,String field){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            Long isSuccess = shardedJedis.hsetnx(key,field,"1");
            if(isSuccess==1){
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            safeClose(shardedJedis);
        }
        return false;
    }
    public void tryFreeLock(String key,String field){
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.hdel(key,field);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            safeClose(shardedJedis);
        }
    }

    public Set getJiaojiAmongSets(List<Set<Integer>> sets){
        if(sets.size()<=1){
            return Collections.emptySet();
        }
        List<Integer> result = new ArrayList<>();
        Set<Integer> targetSet = sets.get(0);
        for (int i=1;i<sets.size();i++){
            Set<Integer> nowSet = sets.get(i);
            targetSet=getJiaoji(targetSet,nowSet);
        }
        return targetSet;
    }
    public Set<Integer> getJiaoji(Set<Integer> collection1,Set<Integer> collection2){
        HashSet<Integer> hashSet;
        if(collection1.size()<collection2.size()){
            hashSet = new HashSet<>(collection1);
            for (Integer integer:collection2){
                if(!hashSet.contains(integer)){
                    collection2.remove(integer);
                }
            }
            return collection2;
            }
        else {
            hashSet = new HashSet<>(collection2);
            for (Integer integer:collection1){
                if(!hashSet.contains(integer)){
                    collection1.remove(integer);
                }
            }
            return collection1;
        }
    }
}


