package com.my.blog.exception;

import com.my.blog.enums.AppHttpCodeEnum;
import lombok.Getter;

@Getter
public class SystemException extends RuntimeException{
    private int code;
    private String msg;

    public SystemException(AppHttpCodeEnum appHttpCodeEnum) {
        super(appHttpCodeEnum.getMsg());
        this.code = appHttpCodeEnum.getCode();
        this.msg = appHttpCodeEnum.getMsg();
    }
}
