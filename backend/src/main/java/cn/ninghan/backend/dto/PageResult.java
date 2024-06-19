package cn.ninghan.backend.dto;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@Builder
@ToString
public class PageResult<T> {
    private List<T> data = Lists.newArrayList();
    private int total;
}
