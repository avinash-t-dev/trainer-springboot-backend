package com.examly.springapp.exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionHandler.class); 

    @ExceptionHandler(DuplicateFeedbackException.class)
    public ResponseEntity<String> handleDuplicateFeedbackException(DuplicateFeedbackException ex) {
        logger.error("Duplicate feedback error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(409));
    }

    @ExceptionHandler(DuplicateTrainerException.class)
    public ResponseEntity<String>handelDuplicateTrainerException(DuplicateTrainerException exception){
        logger.error("Duplicate trainer error: {}", exception.getMessage());
    return new ResponseEntity<>(exception.getMessage(),HttpStatusCode.valueOf(409));
    }

    @ExceptionHandler(TrainerDeletionException.class)
    public ResponseEntity<String>handelTrainerDeletionException(TrainerDeletionException exception){
        logger.error("Trainer deletion error: {}", exception.getMessage());
    return new ResponseEntity<>(exception.getMessage(),HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<String>handelTrainerNotFoundException(TrainerNotFoundException exception){
        logger.error("Trainer not found error: {}", exception.getMessage());
    return new ResponseEntity<>(exception.getMessage(),HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(DuplicateRequirementException.class)
    public ResponseEntity<String> handleDuplicateRequirementException(DuplicateRequirementException ex) {
        logger.error("Duplicate requirement error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(409));
    }

    @ExceptionHandler(RequirementDeletionException.class)
    public ResponseEntity<String> handleRequirementDeletionException(RequirementDeletionException ex) {
        logger.error("Requirement deletion error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(404));
    }

    @ExceptionHandler(RequirementNotFoundException.class)
    public ResponseEntity<String> handleRequirementNotFoundException(RequirementNotFoundException ex) {
        logger.error("Requirement not found error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(404));
    }


    @ExceptionHandler(FeedbackNotFoundException.class)
    public ResponseEntity<String> handleFeedbackNotFoundException(FeedbackNotFoundException ex) {   
        logger.error("Feedback not found error: {}", ex.getMessage());
       return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(404));
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUser(DuplicateUserException ex)
    {
        logger.error("Duplicate user error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(409));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex)
    {
        logger.error("User not found error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(404));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatch(PasswordMismatchException ex)
    {
        logger.error("Password mismatch error: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.valueOf(403));
    }


}
