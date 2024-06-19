package cn.ninghan.backend.service;

import cn.ninghan.common.domain.ClassroomSeat;
import cn.ninghan.common.seatDao.ClassroomSeatMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TransactionService {
    @Resource
    ClassroomSeatMapper classroomSeatMapper;
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<ClassroomSeat> seats){
        List<List<ClassroomSeat>> partition = Lists.partition(seats,100);
        partition.parallelStream().forEach(partitionList->{
            classroomSeatMapper.batchInsert(partitionList);
        });
    }
}
