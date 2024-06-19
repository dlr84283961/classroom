package cn.ninghan.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassroomSeatDto {
    private Long id;
    private String time;
    private Long userId;
    private String classroomNumber;
    private Integer classroomNumberId;
    private Integer carriageNumber;
    private Integer rowNumber;
    private Integer seatNumber;
    private String startTime;
    private String endTime;
    private String showNumber;
    private Integer status;
}
