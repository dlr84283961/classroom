package cn.ninghan.front.service;

import cn.ninghan.front.domain.CanSelectTime;
import com.google.common.collect.Lists;
import jdk.nashorn.internal.runtime.ListAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class CanSelectTimeService {
    public List<CanSelectTime> getCanSelectTimes(int timePeriod){
        String startTime = "10:00";
        String endTime = "22:00";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime tempLocalTime = LocalTime.parse(startTime,dateTimeFormatter);
        LocalTime endLocalTime = LocalTime.parse(endTime,dateTimeFormatter);
        List<CanSelectTime> canSelectTimes = Lists.newArrayList();
        while (endLocalTime.compareTo(tempLocalTime)>=0){
            CanSelectTime canSelectTime = new CanSelectTime();
            canSelectTime.setSelectTime(tempLocalTime.format(dateTimeFormatter));
            canSelectTimes.add(canSelectTime);
            tempLocalTime = tempLocalTime.plusMinutes(timePeriod);
        }
//        CanSelectTime canSelectTime = CanSelectTime.builder().selectTime(endTime).build();
//        canSelectTimes.add(canSelectTime);
        return canSelectTimes;
    }
}
