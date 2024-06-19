package cn.ninghan.backend.service;

import cn.ninghan.backend.common.R;
import cn.ninghan.backend.dto.ClassroomSeatDto;
import cn.ninghan.backend.param.PageParam;
import cn.ninghan.backend.param.SeatSearchParam;
import cn.ninghan.backend.utils.BusinessException;
import cn.ninghan.common.dao.ClassroomNumberMapper;
import cn.ninghan.common.domain.ClassroomNumber;
import cn.ninghan.common.domain.ClassroomSeat;
import cn.ninghan.common.seatDao.ClassroomSeatMapper;
import cn.ninghan.common.utils.ClassroomSeatConstant;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClassroomSeatService {
    @Resource
    private ClassroomSeatMapper classroomSeatMapper;
    @Resource
    private ClassroomNumberMapper classroomNumberMapper;
    @Resource
    private TransactionService transactionService;
    public void generate(Integer classroomId, String startTime,String endTime,int timePeriod){
        ClassroomNumber classroomNumber = classroomNumberMapper.selectByPrimaryKey(classroomId);
        if(classroomNumber==null){
            throw new BusinessException("该教室不存在");
        }
        Table<Integer,Integer, Pair<Integer,Integer>> seatSituation = ClassroomSeatConstant.getSeatTable(classroomNumber.getSeatType());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startLocalTime = LocalDateTime.parse(startTime,dateTimeFormatter);
        LocalDateTime endLocalTime = LocalDateTime.parse(endTime,dateTimeFormatter);
        ZoneId zoneId = ZoneId.systemDefault();
        if(endLocalTime.isBefore(startLocalTime)){
            throw new BusinessException("结束时间不能比开始时间晚");
        }
        List<ClassroomSeat> seats = Lists.newArrayList();
        for (Table.Cell<Integer,Integer,Pair<Integer,Integer>> cell:seatSituation.cellSet()){
            Integer carriage = cell.getColumnKey();
            Integer row = cell.getRowKey();
            Pair<Integer,Integer> seatNum = cell.getValue();
            for (int i = seatNum.getKey();i<=seatNum.getValue();i++){
                LocalDateTime tmpTime = LocalDateTime.parse(startTime,dateTimeFormatter);
                while (endLocalTime.compareTo(tmpTime)>=0){
                    ClassroomSeat seat = ClassroomSeat.builder().time(startLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                            .startTime(Date.from(tmpTime.atZone(zoneId).toInstant()))
                            .endTime(Date.from(tmpTime.plusMinutes(timePeriod).atZone(zoneId).toInstant()))
                            .carriageNumber(carriage).rowNumber(row).seatNumber(i).status(0)
                            .showNumber(carriage+"行"+row+"列"+i+"号")
                            .classroomNumberId(classroomId).build();
                    seats.add(seat);
                    tmpTime = tmpTime.plusMinutes(timePeriod);
                }
            }
        }
        transactionService.batchInsert(seats);
    }

    public List<ClassroomSeatDto> searchWithPage(SeatSearchParam seatSearchParam, PageParam pageParam){
        ClassroomNumber classroomNumber = classroomNumberMapper.findByName(seatSearchParam.getClassroomNumber());
        if(classroomNumber==null){
            throw new BusinessException("该教室不存在");
        }
        List<ClassroomSeat> originSeats = classroomSeatMapper.searchList(classroomNumber.getId(),
                seatSearchParam.getTime(),seatSearchParam.getStatus(),seatSearchParam.getCarriageNum(),
                seatSearchParam.getRowNum(),seatSearchParam.getSeatNum(),pageParam.getOffset(),pageParam.getPageSize());
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<ClassroomSeatDto> classroomSeatDtos = originSeats.stream().map(it->{
            ClassroomSeatDto classroomSeatDto = new ClassroomSeatDto();
            classroomSeatDto.setId(it.getId());
            classroomSeatDto.setClassroomNumber(classroomNumber.getName());
            classroomSeatDto.setTime(it.getTime());
            classroomSeatDto.setStatus(it.getStatus());
            classroomSeatDto.setCarriageNumber(it.getCarriageNumber());
            classroomSeatDto.setRowNumber(it.getRowNumber());
            classroomSeatDto.setSeatNumber(it.getSeatNumber());
            classroomSeatDto.setStartTime(LocalDateTime.ofInstant(it.getStartTime().toInstant(),zoneId).format(formatter));
            classroomSeatDto.setEndTime(LocalDateTime.ofInstant(it.getEndTime().toInstant(),zoneId).format(formatter));
            return classroomSeatDto;
        }).collect(Collectors.toList());
        return classroomSeatDtos;
    }

    public int searchCount(SeatSearchParam param){
        ClassroomNumber classroomNumber = classroomNumberMapper.findByName(param.getClassroomNumber());
        if(classroomNumber==null){
            throw new BusinessException("该教室不存在");
        }
        int count = classroomSeatMapper.countList(classroomNumber.getId(),param.getTime(),param.getStatus(),param.getCarriageNum(),param.getRowNum(),param.getSeatNum());
        return count;
    }

    public void publishSeats(String classroomNumber, String classroomSeatIds) {
        ClassroomNumber classroom = classroomNumberMapper.findByName(classroomNumber);
        if(classroom==null){
            throw new BusinessException("该教室不存在");
        }
        List<String> ids = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(classroomSeatIds);
        List<Long> longIds = ids.stream().map(it->{
            return Long.valueOf(it);
        }).collect(Collectors.toList());
        classroomSeatMapper.batchPublish(classroom.getId(),longIds);
    }

    public void initializeSeats(String classroomNumber, String classroomSeatIds) {
        ClassroomNumber classroom = classroomNumberMapper.findByName(classroomNumber);
        if(classroom==null){
            throw new BusinessException("该教室不存在");
        }
        List<String> ids = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(classroomSeatIds);
        List<Long> longIds = ids.stream().map(it->{
            return Long.valueOf(it);
        }).collect(Collectors.toList());
        classroomSeatMapper.batchInitialize(classroom.getId(),longIds);
    }
}
