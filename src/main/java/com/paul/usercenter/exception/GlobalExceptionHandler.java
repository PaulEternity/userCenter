package com.paul.usercenter.exception;

import com.paul.usercenter.common.BaseResponse;
import com.paul.usercenter.common.ErrorCode;
import com.paul.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("businessException{}", e.getDescription(), e);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR,e.getMessage(),e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.PARAMS_ERROR,e.getMessage(),"");
    }
}
