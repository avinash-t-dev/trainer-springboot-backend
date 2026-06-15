package com.examly.springapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Data
@NoArgsConstructor
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    private String feedbackText;
    private LocalDate date;
    private String category; 

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;

    @JsonProperty("userId")
    public Long getUserId() {
    return user != null ? user.getUserId() : null;}


    @ManyToOne
    @JoinColumn(name = "trainerId", nullable = true)
    @JsonIgnore
    private Trainer trainer;

    @JsonProperty("trainerId")
    public Long getTrainerId() {
    return trainer != null ? trainer.getTrainerId() : null;}


}
