package com.newstorm.common;

import java.io.Serializable;

/**
 * @author NewStorm
 * @description
 * 封装统一格式的数据返回结构
 * 由于封装的 JSON 数据的类型不确定，所以在定义统一的 JSON 结构时，
 * 我们需要用到泛型。统一的 JSON 结构中属性包括数据、状态码、提示信息即可，
 * 构造方法可以根据实际业务需求做相应的添加即可，
 * 一般来说，应该有默认的返回结构，也应该有用户指定的返回结构。
 */
public class JsonResult implements Serializable {
    public static final int SUCCESS = 0;
    public static final int ERROR = 1;

    private int state;
    /** 错误消息  */
    private String message;
    /** 返回正确时候的数据 */
    private Object data;

    public JsonResult() {
    }

    public JsonResult(String error){
        state = ERROR;
        this.message = error;
    }

    public JsonResult(Object data){
        state = SUCCESS;
        this.data = data;
    }

    public JsonResult(Throwable e) {
        state = ERROR;
        message = e.getMessage();
    }

    public JsonResult(int state, Throwable e) {
        this.state = state;
        this.message = e.getMessage();
    }

    public static JsonResult success() {
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = SUCCESS;
        jsonResult.message = "成功";
        return jsonResult;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JsonResult [state=" + state + ", message=" + message + ", data=" + data + "]";
    }
}
