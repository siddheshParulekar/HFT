package com.thrift.hft.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Incorrect Email ID Please use registered email id only")
public class IncorrectEmailIdException extends RuntimeException{
}

