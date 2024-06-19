package cn.ninghan.front.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassroomSeatLeftDto {
    private Integer id;
    private String number;
    private Integer leftCount;
}
