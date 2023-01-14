package com.app.warehouse.sockswarehouse.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class JsonIOException extends RuntimeException{

    public JsonIOException() {
    }

    public JsonIOException(String message) {
        super(message);
    }

    public JsonIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonIOException(Throwable cause) {
        super(cause);
    }
}
