package cn.ninghan.canal.service;

import cn.ninghan.canal.domain.ClassroomSeatRedisDto;
import cn.ninghan.canal.redis.RedisUtil;
import cn.ninghan.common.dao.ClassroomNumberMapper;
import cn.ninghan.common.domain.ClassroomNumber;
import cn.ninghan.common.domain.ClassroomSeat;
import cn.ninghan.common.utils.TimeUtil;
import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class ClassroomSeatHandler {
    @Resource
    private ClassroomNumberMapper classroomNumberMapper;
    @Resource
    private RedisUtil redisUtil;
    public void handleColumn(CanalEntry.RowData rowData, CanalEntry.EventType eventType) {
        if (eventType != CanalEntry.EventType.UPDATE) {
            log.warn("非更新操作，无需处理");
        }
        ClassroomSeatRedisDto classroomSeat = new ClassroomSeatRedisDto();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            if (column.getName().equals("status")) {
                classroomSeat.setStatus(Integer.valueOf(column.getValue()));
            } else if (column.getName().equals("open_time")) {
                classroomSeat.setTime(column.getValue());
            } else if (column.getName().equals("start_time")) {
                //log.info(column.getValue());
                String timestr = column.getValue();
                LocalDateTime localDateTime = LocalDateTime.parse(timestr, dateTimeFormatter);
                String timeStr = TimeUtil.fillTimeStrWith0(localDateTime.getHour(),localDateTime.getMinute());
                classroomSeat.setStartTime(timeStr);
            } else if (column.getName().equals("end_time")) {
                //log.info(column.getValue());
                LocalDateTime localDateTime = LocalDateTime.parse(column.getValue(), dateTimeFormatter);
                String timeStr = TimeUtil.fillTimeStrWith0(localDateTime.getHour(),localDateTime.getMinute());
                classroomSeat.setEndTime(timeStr);
            } else if (column.getName().equals("classroom_number_id")) {
                classroomSeat.setClassroomNumberId(Integer.valueOf(column.getValue()));
            } else if (column.getName().equals("carriage_number")) {
                classroomSeat.setCarriageNumber(Integer.parseInt(column.getValue()));
            } else if (column.getName().equals("rowNumber")) {
                classroomSeat.setRowNumber(Integer.parseInt(column.getValue()));
            } else if (column.getName().equals("seat_number")) {
                classroomSeat.setSeatNumber(Integer.parseInt(column.getValue()));
            }
        }

        ClassroomNumber classroomNumber = classroomNumberMapper.selectByPrimaryKey(classroomSeat.getClassroomNumberId());
        if(classroomSeat.getStatus()==1){
            redisUtil.setAddOne(classroomNumber.getName() + "_" + classroomSeat.getTime() + "_" + classroomSeat.getStartTime(), classroomSeat.getSeatNumber());
        }else{
            redisUtil.setDeleteOne(classroomNumber.getName() + "_" + classroomSeat.getTime() + "_" + classroomSeat.getStartTime(),classroomSeat.getSeatNumber());
        }
    }
}
