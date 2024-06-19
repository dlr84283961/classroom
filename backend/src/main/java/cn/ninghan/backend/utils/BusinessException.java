package cn.ninghan.backend.utils;

public class BusinessException extends RuntimeException{
    public BusinessException(){
        super();
    }
    public BusinessException(String msg){
        super(msg);
    }
    public BusinessException(Throwable throwable){
        super(throwable);
    }
    public BusinessException(String msg,Throwable throwable){
        super(msg,throwable);
    }
    protected BusinessException(String msg,Throwable throwable,boolean enableSuppression, boolean writableStackTrace){
        super(msg,throwable,enableSuppression,writableStackTrace);
    }
}
