package com.mishchenkov.service.repository.exception;

public class TechnicalProblemServiceException extends ServiceException {
    public TechnicalProblemServiceException(String message) {
        super(message);
    }

    public TechnicalProblemServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TechnicalProblemServiceException(Throwable cause) {
        super(cause);
    }
}
