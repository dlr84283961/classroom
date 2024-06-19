package cn.ninghan.backend.param;

import lombok.Data;

@Data
public class PageParam {
    private Integer pageNo;
    private Integer pageSize;
    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
