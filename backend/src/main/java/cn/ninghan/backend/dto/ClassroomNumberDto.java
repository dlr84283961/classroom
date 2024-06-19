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
public class ClassroomNumberDto {
    private Integer id;

    private String name;

    private String startTime;

    private String endTime;

    private String seatType;

    private Short roomType;

    private Integer seatNum;
}
