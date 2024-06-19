package cn.ninghan.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomUser {
    private Long id;

    private String name;

    private String password;

    private String telephone;

    private String mail;

    private Integer status;

}