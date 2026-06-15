

package com.examly.springapp.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.examly.springapp.exceptions.DuplicateRequirementException;
import com.examly.springapp.exceptions.RequirementDeletionException;
import com.examly.springapp.exceptions.RequirementNotFoundException;
import com.examly.springapp.model.Requirement;
import com.examly.springapp.model.RequirementRequestDto;
import com.examly.springapp.model.Trainer;
import com.examly.springapp.repository.RequirementRepository;
import com.examly.springapp.repository.TrainerRepository;

@Service
public class RequirementServiceImpl implements RequirementService {


    private static final Logger logger = LoggerFactory.getLogger(RequirementServiceImpl.class);

    private RequirementRepository requirementRepo;

    private TrainerRepository trainerRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepo,TrainerRepository trainerRepository) {
        this.requirementRepo = requirementRepo;
        this.trainerRepository=trainerRepository;
    }

   


    @Override
    public Requirement addRequirement(Requirement requirement) {

    if (requirementRepo.existsByTitle(requirement.getTitle()))
    {
        throw new DuplicateRequirementException("Requirement already exists");
    }  
    return requirementRepo.save(requirement);
    }

    @Override
    public Optional<Requirement> getRequirementById(Long requirementId) 
    {
        Optional<Requirement> req = requirementRepo.findById(requirementId);
        if (req.isEmpty()) {
            throw new RequirementNotFoundException("Requirement not found with ID: " + requirementId);
        }
        return req;
    }

    @Override
    public List<Requirement> getAllRequirements() {
        return requirementRepo.findAll();
    }

    @Override
    public Requirement updateRequirement(Long requirementId, RequirementRequestDto requirementRequestDto) {

 

        Trainer trainer=trainerRepository.findById(requirementRequestDto.getTrainerId()).orElse(null);

        Requirement requirement=requirementRepo.findById(requirementId).orElseThrow(()->new RequirementNotFoundException("Requirement not found"));
        
        if (!requirementRepo.existsById(requirementId)) {
            throw new RequirementNotFoundException("Requirement not found with ID: " + requirementId);
        }

        requirement.setTrainer(trainer);

        requirement.setRequirementId(requirementId);
        requirement.setStatus(requirementRequestDto.getStatus());

        requirement.setTitle(requirementRequestDto.getTitle());
        requirement.setBudget(requirementRequestDto.getBudget());
        requirement.setDepartment(requirementRequestDto.getDepartment());
        requirement.setDescription(requirementRequestDto.getDescription());
        requirement.setDuration(requirementRequestDto.getDuration());
        requirement.setLocation(requirementRequestDto.getLocation());
        requirement.setMode(requirementRequestDto.getMode());
        requirement.setPostedDate(requirementRequestDto.getPostedDate());
        requirement.setPriority(requirementRequestDto.getPriority());
        requirement.setSkillLevel(requirementRequestDto.getSkillLevel());

        return requirementRepo.save(requirement);
    }


    @Override
    public Requirement deleteRequirement(Long requirementId) {
      

        Requirement req = requirementRepo.findById(requirementId).orElse(null);

        if (req==null) 
        {
            throw new RequirementDeletionException("Requirement not found");
        }

        req.setTrainer(null);

        requirementRepo.delete(req);
        return req;
    }

    @Override
    public List<Requirement> getRequirementsByTrainerId(Long trainerId) {


        List<Requirement> requirements = requirementRepo.findAllByTrainerTrainerId(trainerId);

        if(requirements.isEmpty()) 
        {
            throw new RequirementNotFoundException("No requirements found for trainer with ID: " + trainerId);
        }

        return requirements;
    }

    @Override
    public Page<Requirement> getRequirementsByPages(int page, int size) {
        Pageable pageable=PageRequest.of(page, size);
        return requirementRepo.findAll(pageable);

    }


}
