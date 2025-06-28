package com.fintech.cms.exception;

public class CardException extends RuntimeException {
    
    public CardException(String message) {
        super(message);
    }
    
    public CardException(String message, Throwable cause) {
        super(message, cause);
    }
}