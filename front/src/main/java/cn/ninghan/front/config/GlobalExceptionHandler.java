package cn.ninghan.front.config;

import cn.ninghan.front.common.R;
import cn.ninghan.front.exception.FrontBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public R businessExceptionHandler(RuntimeException e){
        log.error(e.getMessage());
        if(e instanceof FrontBusinessException){
            return R.fail(e.getMessage());
        }
        return R.fail("系统异常，请稍后再试");
    }

//    @ExceptionHandler(value = Error.class)
//    @ResponseBody
//    public R errorHandler(Error e){
//        log.error("系统异常");
//        return R.fail("系统异常");
//    }
}
