package cn.ninghan.front.service;

import cn.ninghan.common.dao.ClassroomNumberMapper;
import cn.ninghan.common.domain.ClassroomNumber;
import cn.ninghan.common.domain.ClassroomSeat;
import cn.ninghan.common.seatDao.ClassroomSeatMapper;
import cn.ninghan.common.utils.TimeUtil;
import cn.ninghan.front.domain.ClassroomSeatLeftDto;
import cn.ninghan.front.domain.FrontSeatDto;
import cn.ninghan.front.exception.FrontBusinessException;
import cn.ninghan.front.redis.RedisUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClassroomSeatService {
    @Resource
    private ClassroomSeatMapper classroomSeatMapper;
    @Resource
    private ClassroomNumberMapper classroomNumberMapper;
    @Resource
    private ClassroomNumberCacheService classroomNumberCacheService;
    @Resource
    private RedisUtil redisUtil;
    public List<ClassroomSeatLeftDto> getSeatsLeft(String startTime,String endTime,String date,int timePeriod){
        List<ClassroomNumber> classroomNumbers = classroomNumberMapper.getAll();
        List<ClassroomSeatLeftDto> classroomSeatLeftDtos = Lists.newArrayList();
        LocalTime startLocalTime = LocalTime.parse(startTime, TimeUtil.HH_mm);
        LocalTime endLocalTime = LocalTime.parse(endTime, TimeUtil.HH_mm);
        if(endLocalTime.compareTo(startLocalTime)<=0){
            throw new FrontBusinessException("开始时间应该比结束时间早");
        }
        List<String> timeStrs = Lists.newArrayList();
        LocalTime tmpTime = startLocalTime;
        while (endLocalTime.compareTo(tmpTime)>0){
            String tmpStr = date+"_"+tmpTime.format(TimeUtil.HH_mm);
            timeStrs.add(tmpStr);
            tmpTime = tmpTime.plusMinutes(timePeriod);
        }
        classroomNumbers.forEach(it->{
            ClassroomSeatLeftDto classroomSeatLeftDto = new ClassroomSeatLeftDto();
            String classroomNumber = it.getName();
            List<Set<Integer>> setList = Lists.newArrayList();
            timeStrs.forEach(timeStr->{
                Set<String> set = redisUtil.getSet(classroomNumber+"_"+timeStr);
                if(!CollectionUtils.isEmpty(set)){
                    Set<Integer> integerSet = set.stream().map(item->{
                        return Integer.parseInt(item);
                    }).collect(Collectors.toSet());
                    setList.add(integerSet);
                }
            });
            Set<Integer> finalSet = redisUtil.getJiaojiAmongSets(setList);
            if(finalSet.size()>0){
                classroomSeatLeftDto.setId(it.getId());
                classroomSeatLeftDto.setNumber(classroomNumber);
                classroomSeatLeftDto.setLeftCount(finalSet.size());
                classroomSeatLeftDtos.add(classroomSeatLeftDto);
            }

        });
        return classroomSeatLeftDtos;
    }

    public List<FrontSeatDto> getSeatsByClassroomNumber(String startTime, String endTime, String date, String classroomNumber,int timePeriod) {
        List<String> needStartTimes = Lists.newArrayList();
        LocalTime startLocalTime = LocalTime.parse(startTime,TimeUtil.HH_mm);
        LocalTime endLocalTime = LocalTime.parse(endTime,TimeUtil.HH_mm);
        LocalTime tmpLocalTime = startLocalTime;
        List<FrontSeatDto> frontSeatDtos = Lists.newArrayList();
        while (endLocalTime.compareTo(tmpLocalTime)>0){
            String timeStr = classroomNumber+"_"+date+"_"+tmpLocalTime.format(TimeUtil.HH_mm);
            needStartTimes.add(timeStr);
            tmpLocalTime=tmpLocalTime.plusMinutes(timePeriod);
        }
        List<Set<Integer>> setList = Lists.newArrayList();
        needStartTimes.forEach(str->{
            Set<String> set = redisUtil.getSet(str);
            if(!CollectionUtils.isEmpty(set)){
                Set<Integer> integerSet = set.stream().map(item->{
                    return Integer.parseInt(item);
                }).collect(Collectors.toSet());
                setList.add(integerSet);
            }
        });
        Set<Integer> canGrabSeatsSet = redisUtil.getJiaojiAmongSets(setList);
        if(canGrabSeatsSet!=null&&!CollectionUtils.isEmpty(canGrabSeatsSet)){
            canGrabSeatsSet.forEach(it->{
                FrontSeatDto frontSeatDto = new FrontSeatDto();
                frontSeatDto.setClassroomNumber(classroomNumber);
                frontSeatDto.setSeatNumber(classroomNumber);
                frontSeatDto.setSeatNumber(it.toString());
                frontSeatDto.setDate(date);
                frontSeatDto.setTimePeriod(startTime+"-"+endTime);
                frontSeatDtos.add(frontSeatDto);
            });
        }
        return frontSeatDtos;
    }

    private List<FrontSeatDto> getOtherSeats(List<String> allTimeList,int scale){
        if(allTimeList==null||CollectionUtils.isEmpty(allTimeList)){
            return Collections.emptyList();
        }
        int scaleSize = allTimeList.size()/scale;

        for(int i=0;i<allTimeList.size();i++){
            if((i+scaleSize+1)>allTimeList.size()){
                break;
            }
        }
        return null;
    }

    public void grabSeat(String classroomNumber, String date, String startTime, String endTime, Integer seatNumber,Integer timePeriod) {
        ClassroomNumber classroomNumberDomain = classroomNumberCacheService.getClassroomByNumber(classroomNumber);
        if(classroomNumber==null){
            throw new FrontBusinessException("该教室不存在");
        }
        List<Date> needStartTimes = Lists.newArrayList();
        List<String> redisNeedStartTimes = Lists.newArrayList();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalTime startLocalTime = LocalTime.parse(startTime,TimeUtil.HH_mm);
        LocalTime endLocalTime = LocalTime.parse(endTime,TimeUtil.HH_mm);
        LocalTime tmpLocalTime = startLocalTime;
        while (endLocalTime.compareTo(tmpLocalTime)>0){
            String timeStr = date+" "+tmpLocalTime.format(TimeUtil.HH_mm);
            redisNeedStartTimes.add(timeStr);
            LocalDateTime localDateTime = LocalDateTime.parse(timeStr,TimeUtil.yyyy_MM_dd_hh_mm);
            Date startTimeDate = Date.from(localDateTime.atZone(zoneId).toInstant());
            needStartTimes.add(startTimeDate);
            tmpLocalTime=tmpLocalTime.plusMinutes(timePeriod);
        }
        boolean isSuccess = redisUtil.tryLockSeat(classroomNumberDomain.getName(),redisNeedStartTimes,"123");
        if(isSuccess==false){
            redisUtil.freeSeatLock(classroomNumberDomain.getName(),redisNeedStartTimes,"123");
            throw new FrontBusinessException("该时间段已经有用户在占座，请重新选择座位");
        }
        List<ClassroomSeat> seats = classroomSeatMapper.getToPlaceSeatList(classroomNumberDomain.getId(),seatNumber,needStartTimes,date);
        if(seats.size()!=needStartTimes.size()){
            redisUtil.freeSeatLock(classroomNumberDomain.getName(),redisNeedStartTimes,"123");
            throw new FrontBusinessException("该时间段已经有用户在占座，请重新选择座位");
        }
        List<Long> seatIdList = seats.stream().map(it->{
            return it.getId();
        }).collect(Collectors.toList());
        int sqlSuccessCount = classroomSeatMapper.batchPlace(classroomNumberDomain.getId(),seatIdList,123l);
        if(sqlSuccessCount!=seatIdList.size()){
            classroomSeatMapper.batchRollbackPlace2(classroomNumberDomain.getId(),seatIdList,123l);
            redisUtil.freeSeatLock(classroomNumberDomain.getName(),redisNeedStartTimes,"123");
            throw new FrontBusinessException("占座失败，该时间段已经有人占座，请重新选择座位");
        }
        redisUtil.freeSeatLock(classroomNumberDomain.getName(),redisNeedStartTimes,"123");
    }
}
