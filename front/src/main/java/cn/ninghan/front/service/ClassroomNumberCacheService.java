package cn.ninghan.front.service;

import cn.ninghan.common.dao.ClassroomNumberMapper;
import cn.ninghan.common.domain.ClassroomNumber;
import cn.ninghan.front.exception.FrontBusinessException;
import cn.ninghan.front.redis.RedisUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ClassroomNumberCacheService {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ClassroomNumberMapper classroomNumberMapper;
    public ClassroomNumber getClassroomByNumber(String roomNumber){
        String classroomNumberStr = redisUtil.hGet(redisUtil.CLASSROOM_NUMBER_KEY,roomNumber);
        if(StringUtils.isBlank(classroomNumberStr)||classroomNumberStr==null){
            boolean isSuccess = redisUtil.tryLock(redisUtil.LOCK_KEY_CLASSROOM,roomNumber);
            if(isSuccess){
                ClassroomNumber classroomNumber = classroomNumberMapper.findByName(roomNumber);
                if(classroomNumber==null){
                    throw new FrontBusinessException("教室不存在，服务器出错，请稍后再试");
                }
                String saveStr = JSON.toJSONString(classroomNumber);
                redisUtil.hSet(redisUtil.CLASSROOM_NUMBER_KEY,roomNumber,saveStr);
                redisUtil.tryFreeLock(redisUtil.LOCK_KEY_CLASSROOM,roomNumber);
                return classroomNumber;
            }else {
                throw new FrontBusinessException("服务器出错，请稍后再试");
            }
        }
        ClassroomNumber classroomNumber = JSON.parseObject(classroomNumberStr,ClassroomNumber.class);
        return classroomNumber;
    }
}
