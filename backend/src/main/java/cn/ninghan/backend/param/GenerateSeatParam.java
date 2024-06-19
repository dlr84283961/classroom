package cn.ninghan.backend.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class GenerateSeatParam {
    @NotNull(message = "id不能为空")
    private Integer classroomNumberId;

    @NotNull(message = "形式必须为yyyy-MM-dd HH:mm")
    @Length(max = 20,min = 10,message = "形式必须为yyyy-MM-dd HH:mm")
    private String startTime;

    @NotNull(message = "形式必须为yyyy-MM-dd HH:mm")
    @Length(max = 20,min = 10,message = "形式必须为yyyy-MM-dd HH:mm")
    private String endTime;
}
