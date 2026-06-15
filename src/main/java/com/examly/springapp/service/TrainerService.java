package com.examly.springapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Trainer;

@Service
public interface TrainerService {

    Trainer addTrainer(Trainer trainer);

    Optional<Trainer> getTrainerById(Long trainerId);

    List<Trainer> getAllTrainers();

    Trainer updateTrainer(Long trainerId,Trainer trainer);

    Trainer deleteTrainer(Long trainerId);

    //pagination

    Page<Trainer> getTrainersByPage(int page,int size);



}
