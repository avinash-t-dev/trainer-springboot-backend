package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.examly.springapp.model.Requirement;
@Repository
public interface RequirementRepository  extends JpaRepository<Requirement,Long>{

    boolean existsByTitle(String title);

    List<Requirement> findAllByTrainerTrainerId(Long trainerId);
}