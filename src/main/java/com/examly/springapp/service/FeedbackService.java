package com.examly.springapp.service;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.FeedbackRequestDto;

@Service
public interface FeedbackService {
    
    Feedback createFeedback(FeedbackRequestDto feedbackRequestDto);
    Feedback getFeedbackById(Long feedbackId);
    List<Feedback> getAllFeedbacks();
    void deleteFeedback(Long feedbackId);
    List<Feedback> getFeedbacksByUserId(Long userId);
    void addBulkFeedbacks(List<FeedbackRequestDto> feedbacks);
    Page<Feedback> getFeedbacksByPage(int page,int size);


}