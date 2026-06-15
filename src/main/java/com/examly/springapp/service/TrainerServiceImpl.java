package com.examly.springapp.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.examly.springapp.exceptions.DuplicateTrainerException;
import com.examly.springapp.exceptions.TrainerDeletionException;
import com.examly.springapp.exceptions.TrainerNotFoundException;
import com.examly.springapp.model.Requirement;
import com.examly.springapp.model.Trainer;
import com.examly.springapp.repository.RequirementRepository;
import com.examly.springapp.repository.TrainerRepository;


@Service
public class TrainerServiceImpl implements TrainerService{

    private static final Logger logger=LoggerFactory.getLogger(TrainerServiceImpl.class);

    private TrainerRepository trainerRepo;

    private RequirementRepository requirementRepository;


    public TrainerServiceImpl(TrainerRepository trainerRepo,RequirementRepository requirementRepository) {
        this.trainerRepo = trainerRepo;
        this.requirementRepository=requirementRepository;
    }

   

    @Override
    public Trainer addTrainer(Trainer trainer) {

        List<Trainer>trainersWithEmailId=trainerRepo.findByEmail(trainer.getEmail());
        if(!trainersWithEmailId.isEmpty()){
            throw new DuplicateTrainerException("Trainer Already Exists with this email: "+trainer.getEmail());
        }
        return trainerRepo.save(trainer);
    }

    @Override
    public Optional<Trainer> getTrainerById(Long trainerId) {
        Optional<Trainer>trainersById=trainerRepo.findById(trainerId);

        if(trainersById.isEmpty())
        {
            throw new TrainerNotFoundException("No Trainer Found with the Trainer ID: "+trainerId);
        }
        return trainersById;

    }

    @Override
    public List<Trainer> getAllTrainers() {
        List<Trainer>trainers= trainerRepo.findAll();
        return trainers;
    }

    @Override
    public Trainer updateTrainer(Long trainerId, Trainer trainer) {
        Trainer existingTrainer=trainerRepo.findById(trainerId).orElse(null);
        if(existingTrainer==null){
            
            throw new TrainerNotFoundException("No Trainer Found with the Trainer ID: "+trainerId);
        }
        else{

            existingTrainer.setCertification(trainer.getCertification());
            existingTrainer.setEmail(trainer.getEmail());
            existingTrainer.setExperience(trainer.getExperience());
            existingTrainer.setExpertise(trainer.getExpertise());
            existingTrainer.setJoiningDate(trainer.getJoiningDate());
            existingTrainer.setName(trainer.getName());
            existingTrainer.setPhone(trainer.getPhone());
            existingTrainer.setResume(trainer.getResume());
            existingTrainer.setStatus(trainer.getStatus());

        }
        return trainerRepo.save(existingTrainer);

    }

    @Override
    public Trainer deleteTrainer(Long trainerId) {
        Trainer existingTrainer=trainerRepo.findById(trainerId).orElse(null);
        if(existingTrainer==null)
        {
            throw new TrainerDeletionException("Failed to delete trainer with ID: " + trainerId);
        }
        if(existingTrainer.getRequirements().isEmpty())
        {
            trainerRepo.delete(existingTrainer);
            
        }
        else
        {
            for(Requirement req: existingTrainer.getRequirements())
            {   
                req.setStatus("Open");
                req.setTrainer(null);
                requirementRepository.save(req);
                
            }
            trainerRepo.save(existingTrainer);
            trainerRepo.delete(existingTrainer);
        }
        
        return existingTrainer;
        
        
        
    }

    @Override
    public Page<Trainer> getTrainersByPage(int page, int size) {
        
        Pageable pageable=PageRequest.of(page, size);
        return trainerRepo.findAll(pageable);

    }

    
}