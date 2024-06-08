package com.thrift.hft.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
public class HttpStatusException extends RuntimeException{

    private HttpStatus code = HttpStatus.INTERNAL_SERVER_ERROR;
    private String reason;

    public HttpStatusException(String reason) {
        super(String.format("HTTP %d - %s", HttpStatus.INTERNAL_SERVER_ERROR.value(), reason));
        this.reason = reason;
    }

    public HttpStatusException(HttpStatus code, String reason) {
        super(String.format("HTTP %d - %s", code.value(), reason));
        this.code = code;
        this.reason = reason;
    }
}
