package com.examly.springapp.model;

import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long requirementId;
    
    private String title;
    private String description;
    private String department;
    private LocalDate postedDate;
    private String status;
    private String duration;
    private String mode;
    private String location;
    private String skillLevel;
    private double budget;
    private String priority;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    @JsonIgnore
    private Trainer trainer;

    @JsonProperty("trainerId")
    public Long getTrainerId() {
    return trainer != null ? trainer.getTrainerId() : null;
}

    
}