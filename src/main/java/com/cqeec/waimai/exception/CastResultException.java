package com.cqeec.waimai.exception;

public class CastResultException extends Exception{
    public CastResultException()
    {
        super();
    }
    public CastResultException(String msg)
    {
        super(msg);
    }
    @Override
    public String getMessage() {
        return "数据集转换为实体类异常";
    }
}
