package com.thrift.hft.exceptions;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends HttpStatusException{
    public AlreadyExistsException(String msg){super(HttpStatus.CONFLICT,msg);}
}
