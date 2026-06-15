package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.examly.springapp.model.Trainer;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {

    List<Trainer> findByEmail(String email);
    
}