package com.mishchenkov.service.repository.exception;

public class CantProcessTaskServiceException extends ServiceException {

    public CantProcessTaskServiceException() {
        super();
    }

    public CantProcessTaskServiceException(String message) {
        super(message);
    }

    public CantProcessTaskServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantProcessTaskServiceException(Throwable cause) {
        super(cause);
    }
}
