package com.examly.springapp.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequestDto {

    private String feedbackText;
    private LocalDate date;
    private String category; 

    private long userId;

    private long trainerId;


}
