package com.examly.springapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.Trainer;
import com.examly.springapp.model.User;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Long>{

    List<Feedback> findByUserUserId(Long userId);

    Optional<Feedback> findByFeedbackTextAndDateAndCategoryAndUserAndTrainer(String feedbackText,LocalDate date,String category,User user,Trainer trainer);

    
}