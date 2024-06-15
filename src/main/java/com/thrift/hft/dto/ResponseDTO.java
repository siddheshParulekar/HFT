package com.thrift.hft.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static com.thrift.hft.constants.GeneralMsgConstant.MSG_SUCCESS;

@NoArgsConstructor
@Data
public class ResponseDTO<T> {

    private int status = HttpStatus.OK.value();
    private T data;
    private String message = MSG_SUCCESS;

    public ResponseDTO(T data, String message) {
        this.data = data;
        this.message = message;
    }
}