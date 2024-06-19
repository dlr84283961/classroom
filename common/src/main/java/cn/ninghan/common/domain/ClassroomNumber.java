package cn.ninghan.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassroomNumber {
    private Integer id;

    private String name;

    private Date startTime;

    private Date endTime;

    private String seatType;

    private Short roomType;

    private Integer seatNum;
}
