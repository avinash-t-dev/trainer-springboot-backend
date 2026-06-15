package com.examly.springapp.exceptions;

public class RequirementNotFoundException extends RuntimeException {
    public RequirementNotFoundException(String message) {
        super(message);
    }

}