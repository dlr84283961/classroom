package cn.ninghan.front.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FrontSeatDto {
    private String classroomNumber;
    private String date;
    private String timePeriod;
    private String seatNumber;
}
