package cn.ninghan.common.seatDao;
import cn.ninghan.common.domain.ClassroomSeat;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ClassroomSeatMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ClassroomSeat record);

    int insertSelective(ClassroomSeat record);

    ClassroomSeat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ClassroomSeat record);

    int updateByPrimaryKey(ClassroomSeat record);

//    @InsertProvider(type = ClassroomSeatProvider.class,method = "batchInsert")
    void batchInsert(@Param("list") List<ClassroomSeat> list);

    List<ClassroomSeat> searchList(@Param("classroomNumberId") int classroomNumberId, @Param("time") String time,
                               @Param("status") Integer status, @Param("carriageNum") Integer carriageNum,
                               @Param("rowNum") Integer rowNum, @Param("seatNum") Integer seatNum,
                               @Param("offset") int offset, @Param("pageSize") int pageSize);

    int countList(@Param("classroomNumberId") int classroomNumberId, @Param("time") String time,
                  @Param("status") Integer status, @Param("carriageNum") Integer carriageNum,
                  @Param("rowNum") Integer rowNum, @Param("seatNum") Integer seatNum);

    int batchPublish(@Param("classroomNumberId") int classroomNumberId, @Param("classroomSeatIdList") List<Long> ClassroomSeatIdList);

    int batchInitialize(@Param("classroomNumberId") int classroomNumberId, @Param("classroomSeatIdList") List<Long> ClassroomSeatIdList);


    List<ClassroomSeat> getToPlaceSeatList(@Param("classroomNumberId") int classroomNumberId, @Param("seatNum") Integer seatNum,
                                           @Param("startTimeList") List<Date> startTimeList, @Param("date") String date);

    int batchPlace(@Param("classroomNumberId") int classroomNumberId, @Param("idList") List<Long> idList,
                   @Param("userId") long userId);

    int batchRollbackPlace(@Param("ClassroomSeat") ClassroomSeat ClassroomSeat, @Param("fromStationIdList") List<Integer> fromStationIdList);

    int batchRollbackPlace2(@Param("classroomNumberId") int classroomNumberId, @Param("idList") List<Long> idList,
                            @Param("userId") long userId);

    void cancelSeat(@Param("trainNum") int trainNumberId,@Param("ticket") String ticket,@Param("carriageNum") Integer carriageNum,
                    @Param("rowNum") Integer rowNum,@Param("seatNum") Integer seatNum,@Param("userId") long userId,@Param("travellerId") long travellerId);

}