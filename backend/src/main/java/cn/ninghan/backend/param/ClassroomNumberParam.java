package cn.ninghan.backend.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class ClassroomNumberParam {
    private Integer id;

    @NotNull(message = "教室名不能为空")
    @Length(max = 10,min = 2,message = "长度要在2到10之间")
    private String name;

    @NotNull(message = "形式必须为yyyy-MM-dd HH:mm")
    @Length(max = 20,min = 10,message = "形式必须为yyyy-MM-dd HH:mm")
    private String startTime;

    @NotNull(message = "形式必须为yyyy-MM-dd HH:mm")
    @Length(max = 20,min = 10,message = "形式必须为yyyy-MM-dd HH:mm")
    private String endTime;

    @NotNull(message = "座位类型不能为空")
    private String seatType;

    @NotNull(message = "教室类型不能为空")
    private Integer roomType;
}
