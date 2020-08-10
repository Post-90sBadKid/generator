package com.wry.generator.exception;

import com.wry.generator.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class CustomControllerAdvice {

    private Logger logger = LoggerFactory
            .getLogger(CustomControllerAdvice.class);

    /**
     * 捕捉自定义异常类 CustomException.class
     */
    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public Result tcErrorHandler(CustomException ex) {
        ex.printStackTrace();
        logger.error(ex.getMsg());

        return Result.error(ex.getCode(), ex.getMsg());

    }

    /**
     * 捕捉全局异常 Exception.class
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Result errorHandler(Exception ex) {
        ex.printStackTrace();
        logger.error(ex.getMessage());
        return Result.error("-1", ex.getMessage());

    }

}
