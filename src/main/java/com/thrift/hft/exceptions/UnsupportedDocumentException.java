package com.thrift.hft.exceptions;

import org.springframework.http.HttpStatus;

public class UnsupportedDocumentException extends HttpStatusException{
    public UnsupportedDocumentException(String message){
        super(HttpStatus.BAD_REQUEST, message);
    }
}
