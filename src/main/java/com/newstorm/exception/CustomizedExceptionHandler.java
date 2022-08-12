package com.newstorm.exception;

import com.newstorm.common.JsonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class CustomizedExceptionHandler {
    /**
     * 注解@ExceptionHandler相当于controller的@RequestMapping
     * 如果抛出的的是BaseException，则调用该方法
     * @param exception 业务异常
     * @return JsonResult
     */
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public JsonResult handle(BaseException exception){
        return new JsonResult(exception.getMessage());
    }
}
