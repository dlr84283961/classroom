package cn.ninghan.backend.service;

import cn.ninghan.backend.dto.ClassroomNumberDto;
import cn.ninghan.backend.utils.BusinessException;
import cn.ninghan.common.dao.ClassroomNumberMapper;
import cn.ninghan.common.domain.ClassroomNumber;
import cn.ninghan.backend.param.ClassroomNumberParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClassroomNumberService {
    @Resource
    private ClassroomNumberMapper classroomNumberMapper;
    public List<ClassroomNumberDto> getAll(){
        List<ClassroomNumber> classroomNumbers = classroomNumberMapper.getAll();
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<ClassroomNumberDto> dtos = classroomNumbers.stream().map(it->{
            ClassroomNumberDto dto = new ClassroomNumberDto();
            dto.setId(it.getId());
            dto.setName(it.getName());
            dto.setRoomType(it.getRoomType());
            dto.setSeatType(it.getSeatType());
            dto.setSeatNum(it.getSeatNum());
            dto.setStartTime(LocalDateTime.ofInstant(it.getStartTime().toInstant(),zoneId).format(dateTimeFormatter));
            dto.setEndTime(LocalDateTime.ofInstant(it.getEndTime().toInstant(),zoneId).format(dateTimeFormatter));
            return dto;
        }).collect(Collectors.toList());
        return dtos;
    }

    public void save(ClassroomNumberParam param) {
        ClassroomNumber number = classroomNumberMapper.findByName(param.getName());
        if(number!=null){
            throw new BusinessException("教室已经存在");
        }
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startLocaltime = LocalDateTime.parse(param.getStartTime().trim(),formatter);
        LocalDateTime endLocaltime = LocalDateTime.parse(param.getEndTime().trim(),formatter);
        Date startDate = Date.from(startLocaltime.atZone(zoneId).toInstant());
        Date endDate = Date.from(endLocaltime.atZone(zoneId).toInstant());
        if(startDate.after(endDate)){
            throw new BusinessException("开放时间必须比结束时间早");
        }
        ClassroomNumber classroomNumber = ClassroomNumber.builder().id(param.getId()).name(param.getName())
                .startTime(startDate).endTime(endDate).roomType(param.getRoomType().shortValue())
                .seatType(param.getSeatType()).build();
        classroomNumberMapper.insertSelective(classroomNumber);
    }

    public void updateClass(ClassroomNumberParam param){
        ClassroomNumber origin = classroomNumberMapper.findByName(param.getName());
        if(origin!=null&&origin.getId().intValue()!=param.getId().intValue()){
            throw new BusinessException("该教室名已经存在");
        }
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startLocaltime = LocalDateTime.parse(param.getStartTime().trim(),formatter);
        LocalDateTime endLocaltime = LocalDateTime.parse(param.getEndTime().trim(),formatter);
        Date startDate = Date.from(startLocaltime.atZone(zoneId).toInstant());
        Date endDate = Date.from(endLocaltime.atZone(zoneId).toInstant());
        if(startDate.after(endDate)){
            throw new BusinessException("开放时间必须比结束时间早");
        }
        ClassroomNumber classroomNumber = ClassroomNumber.builder().id(param.getId()).name(param.getName())
                .startTime(startDate).endTime(endDate).roomType(param.getRoomType().shortValue())
                .seatType(param.getSeatType()).build();
        classroomNumberMapper.updateByPrimaryKeySelective(classroomNumber);
    }
}
