package cn.ninghan.backend.param;

import lombok.Data;

@Data
public class SeatSearchParam {
    private String classroomNumber;
    private String time;
    private Integer status;
    private Integer carriageNum;
    private Integer rowNum;
    private Integer seatNum;
}
