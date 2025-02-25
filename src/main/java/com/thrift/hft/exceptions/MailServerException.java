package com.thrift.hft.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Not able to Send Email right now. Try after Sometime.")
public class MailServerException extends RuntimeException {
}
