package com.thrift.hft.exceptions;

import com.thrift.hft.constants.ErrorMsgConstants;
import com.thrift.hft.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionAdvisor extends ResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionAdvisor.class);


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        ResponseDTO<Object> responseDTO = new ResponseDTO();
        responseDTO.setStatus(HttpStatus.BAD_REQUEST.value());

        if (Objects.nonNull(ex.getBindingResult().getGlobalError()))
            responseDTO.setMessage(ex.getBindingResult().getGlobalError().getDefaultMessage());
        else
            responseDTO.setMessage(ex.getBindingResult().getFieldError().getDefaultMessage());

        return new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    protected ResponseEntity<Object> handleExceptions(Throwable ex) {
        Class<?> clz = ex.getClass();

        String message = ErrorMsgConstants.ERROR_INTERNAL_SERVER;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (ex instanceof AccessDeniedException) {
            message = "You are not authorized to access this request";
            status = HttpStatus.NOT_ACCEPTABLE;

        } else if (ex instanceof HttpStatusException) {
            HttpStatusException casted = (HttpStatusException) ex;

            status = casted.getCode();
            if(casted.getReason() != null && !casted.getReason().isEmpty())
                message = casted.getReason();

        } else if (clz.isAnnotationPresent(ResponseStatus.class)) {
            ResponseStatus anno = clz.getAnnotation(ResponseStatus.class);

            status = anno.code();

            if (!anno.reason().isEmpty())
                message = anno.reason();
        }

        logger.error("{} - ", status.value(), ex);

        ResponseDTO<Object> ret = new ResponseDTO<>();
        ret.setMessage(message);
        ret.setStatus(status.value());

        return new ResponseEntity<>(ret, status);
    }
}
