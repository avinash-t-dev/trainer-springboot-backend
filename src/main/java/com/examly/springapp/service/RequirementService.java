package com.examly.springapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Requirement;
import com.examly.springapp.model.RequirementRequestDto;

@Service
public interface RequirementService {


    public Requirement addRequirement(Requirement requirement);
    public Optional<Requirement> getRequirementById(Long requirementId);
    public List<Requirement> getAllRequirements();
    public Requirement updateRequirement(Long requirementId, RequirementRequestDto requirementRequestDto);
    public Requirement deleteRequirement(Long requirementId);
    public List<Requirement> getRequirementsByTrainerId(Long trainerId);

    public Page<Requirement> getRequirementsByPages(int page,int size);
  
}
