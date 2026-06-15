package com.examly.springapp.exceptions;

public class DuplicateFeedbackException extends RuntimeException {
    public DuplicateFeedbackException(String message) {
        super(message);
    }

}