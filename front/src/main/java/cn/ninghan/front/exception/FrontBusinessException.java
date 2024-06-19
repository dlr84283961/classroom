package cn.ninghan.front.exception;

public class FrontBusinessException extends RuntimeException{
    public FrontBusinessException() {
        super();
    }

    public FrontBusinessException(String message) {
        super(message);
    }

    public FrontBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrontBusinessException(Throwable cause) {
        super(cause);
    }

    protected FrontBusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
