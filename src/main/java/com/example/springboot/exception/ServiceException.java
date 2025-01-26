package com.example.springboot.exception;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Getter;

/**
 * custom exception
 */
@Getter
public class ServiceException extends RuntimeException {
    private String code;
    public ServiceException(String code, String msg){
        super(msg);
        this.code = code;
    }
}
