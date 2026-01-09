package com.sonda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TablasReferenciasException extends RuntimeException {
    public TablasReferenciasException(String msg) {
        super(msg);
    }
}
