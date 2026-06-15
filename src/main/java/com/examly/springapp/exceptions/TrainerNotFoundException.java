package com.examly.springapp.exceptions;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(String message) {
        super(message);
    }

}