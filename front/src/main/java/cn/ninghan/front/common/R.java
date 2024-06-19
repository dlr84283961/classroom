package cn.ninghan.front.common;

import lombok.Data;

@Data
public class R<T> {
    private Integer code;
    private String msg;
    private T data;
    private boolean ret;
    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.ret = true;
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> success(String msg){
        R<T> r = new R<T>();
        r.ret = true;
        r.msg = msg;
        r.code = 1;
        return r;
    }

    public static <T> R<T> fail(String msg){
        R<T> r = new R<T>();
        r.ret = false;
        r.msg= msg;
        r.code = 0;
        return r;
    }
}
