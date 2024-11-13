package com.cqeec.waimai.util;

public class Result {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public int code;
    public String message;
    // total、rows属性用于easyui datagrid数据
    public int total;
    public Object rows;

    public Result(int code, String message, int total, Object rows) {
        this.code = code;
        this.message = message;
        this.total = total;
        this.rows = rows;
    }

    public Result(int code, String message, Object rows) {
        this.code = code;
        this.message = message;
        this.rows = rows;
    }
    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public Result() {
    }
}
