package com.examly.springapp.service;
import com.examly.springapp.exceptions.DuplicateFeedbackException;
import com.examly.springapp.exceptions.FeedbackNotFoundException;
import com.examly.springapp.exceptions.TrainerNotFoundException;
import com.examly.springapp.exceptions.UserNotFoundException;
import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.FeedbackRequestDto;
import com.examly.springapp.model.Trainer;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.FeedbackRepository;
import com.examly.springapp.repository.TrainerRepository;
import com.examly.springapp.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private FeedbackRepository feedbackRepository;

    private UserRepository userRepository;

    private TrainerRepository trainerRepository;

    private static final Logger logger=LoggerFactory.getLogger(FeedbackServiceImpl.class);


    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,UserRepository userRepository,TrainerRepository trainerRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository=userRepository;
        this.trainerRepository=trainerRepository;
    }

    @Override
    public Feedback createFeedback(FeedbackRequestDto feedbackRequestDto) {

        User u=userRepository.findById(feedbackRequestDto.getUserId()).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Trainer t=trainerRepository.findById(feedbackRequestDto.getTrainerId()).orElseThrow(()->new TrainerNotFoundException("Trainer Not Found"));

        Optional<Feedback>existingFeedback=feedbackRepository.findByFeedbackTextAndDateAndCategoryAndUserAndTrainer(feedbackRequestDto.getFeedbackText(),feedbackRequestDto.getDate(), feedbackRequestDto.getCategory(), u, t);
        if(existingFeedback.isPresent())
        {
            throw new DuplicateFeedbackException("Feedback with same text,date,category by same user for same trainer already exists");
        }

        Feedback feedback=new Feedback();
        feedback.setFeedbackText(feedbackRequestDto.getFeedbackText());
        feedback.setDate(feedbackRequestDto.getDate());
        feedback.setCategory(feedbackRequestDto.getCategory());
       
        feedback.setUser(u);
        
        feedback.setTrainer(t);
        return feedbackRepository.save(feedback);
    }


    @Override
    public Feedback getFeedbackById(Long feedbackId) {
        
        Feedback feedback = feedbackRepository.findById(feedbackId).orElse(null);
        if (feedback == null) {
            throw new FeedbackNotFoundException("Feedback not found with ID: " + feedbackId);
        }
        return feedback;
        
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        if (feedbackList.isEmpty()) {
            throw new FeedbackNotFoundException("No feedbacks found.");
        }
        return feedbackList;
    }

    @Override
    public void deleteFeedback(Long feedbackId) {

        Feedback feedback=feedbackRepository.findById(feedbackId).orElse(null);
        if(feedback==null){
            throw new FeedbackNotFoundException("Feedback not found with ID: " + feedbackId);
        }

        feedbackRepository.deleteById(feedbackId);
    }


    @Override
    public List<Feedback> getFeedbacksByUserId(Long userId) {
        return feedbackRepository.findByUserUserId(userId);
    }

    @Override
    public void addBulkFeedbacks(List<FeedbackRequestDto> feedbacks) {
        
        logger.info("adding bulk feedbacks in service");
        List<Feedback> feedbackEntities = new ArrayList<>();
        for (FeedbackRequestDto dto : feedbacks) {
            User u = userRepository.findById(dto.getUserId()).orElse(null);
            Trainer t = trainerRepository.findById(dto.getTrainerId()).orElse(null);
            Optional<Feedback> existingFeedback = feedbackRepository.findByFeedbackTextAndDateAndCategoryAndUserAndTrainer(
                dto.getFeedbackText(), dto.getDate(), dto.getCategory(), u, t
            );
            if (existingFeedback.isPresent()) {
                throw new DuplicateFeedbackException("Feedback with same text,date,category by same user for same trainer already exists");
            }
            Feedback feedback = new Feedback();
            feedback.setFeedbackText(dto.getFeedbackText());
            feedback.setDate(dto.getDate());
            feedback.setCategory(dto.getCategory());
            feedback.setUser(u);
            feedback.setTrainer(t);
            feedbackEntities.add(feedback);
        }
        feedbackRepository.saveAll(feedbackEntities);
    }

    @Override
    public Page<Feedback> getFeedbacksByPage(int page, int size) {
        
        Pageable pageable=PageRequest.of(page, size);
        return feedbackRepository.findAll(pageable);
    }

 

    
}

