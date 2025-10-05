package com.comic.exception;

import com.comic.vo.ResultVO;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice  // 全局异常处理，只对Controller生效
public class GlobalExceptionHandler {

    // 处理参数校验失败的异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultVO handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        // 获取第一个错误信息
        FieldError firstError = bindingResult.getFieldError();
        String errorMsg = firstError != null ? firstError.getDefaultMessage() : "参数校验失败";
        return ResultVO.error(errorMsg);
    }

    // 处理其他所有异常
    @ExceptionHandler(Exception.class)
    public ResultVO handleException(Exception e) {
        e.printStackTrace();  // 打印异常堆栈，方便调试
        return ResultVO.error("服务器内部错误：" + e.getMessage());
    }
}
