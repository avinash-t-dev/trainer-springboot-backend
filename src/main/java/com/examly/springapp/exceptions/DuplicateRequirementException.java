package com.examly.springapp.exceptions;

public class DuplicateRequirementException extends RuntimeException{
 public DuplicateRequirementException(String message){
    super(message);
 }
}