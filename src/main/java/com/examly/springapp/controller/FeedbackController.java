package com.examly.springapp.controller;


import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.FeedbackRequestDto;
import com.examly.springapp.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    private FeedbackService feedbackService;
    
    public FeedbackController(FeedbackService feedbackService){
        this.feedbackService=feedbackService;
    }
    

    @PostMapping("/feedback")
    public ResponseEntity<Feedback> createFeedback(@RequestBody FeedbackRequestDto feedbackRequestDto) { 
        logger.info("Received feedback request: {}", feedbackRequestDto);
        Feedback createdFeedback=feedbackService.createFeedback(feedbackRequestDto);
        logger.info("Feedback created successfully: {}", createdFeedback);
        return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED); 
        }

    
    @GetMapping("/feedback/{feedbackId}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Long feedbackId) {

        logger.info("Request to get feedback by ID: {}", feedbackId);
        Feedback feedback = feedbackService.getFeedbackById(feedbackId);
        logger.info("Feedback details retrieved: {}", feedback);
        return new ResponseEntity<>(feedback,HttpStatus.valueOf(200));
    }


    @DeleteMapping("/feedback/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long feedbackId) {
        logger.info("Request to delete feedback with ID: {}", feedbackId);
        feedbackService.deleteFeedback(feedbackId);
        logger.info("Feedback with ID: {} deleted successfully", feedbackId);
        return new ResponseEntity<>(HttpStatus.valueOf(200)); 
    }

    @GetMapping("/feedback/user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByUserId(@PathVariable Long userId) {
        logger.info("Request to get feedbacks for user ID: {}", userId);
        List<Feedback> feedbackList = feedbackService.getFeedbacksByUserId(userId);
        logger.info("Feedbacks retrieved for user ID {}: {}", userId, feedbackList);
        return new ResponseEntity<>(feedbackList,HttpStatus.valueOf(200));
    }

    @GetMapping("/feedback")
    public Page<Feedback> getFeedbacksByPages(@RequestParam( defaultValue = "0") int page,@RequestParam( defaultValue = "10") int size)
    {
        logger.info("Fetching feedbacks for page: {} and size: {}", page, size);
        Page<Feedback> pagedFeedbacks=feedbackService.getFeedbacksByPage(page,size);
        logger.info("Fetched feedbacks: {}", pagedFeedbacks);
        return pagedFeedbacks;

    }

}
