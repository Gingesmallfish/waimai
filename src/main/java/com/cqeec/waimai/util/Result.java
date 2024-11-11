package com.cqeec.waimai.util;

public class Result {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public int code;
    public String message;
    public Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public Result() {
    }
}
