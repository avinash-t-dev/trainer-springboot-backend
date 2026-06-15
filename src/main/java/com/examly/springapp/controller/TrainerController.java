package com.examly.springapp.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.Trainer;

import com.examly.springapp.service.TrainerService;


@RestController
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    
    private TrainerService trainerService;
    
    public TrainerController(TrainerService trainerService){
        this.trainerService=trainerService;
    }



    @PostMapping("/api/trainer")
    public ResponseEntity<Trainer>addTrainer(@RequestBody Trainer trainer){
        logger.info("Received request to add trainer: {}", trainer);
        Trainer addedTrainer=trainerService.addTrainer(trainer);
        logger.info("Trainer added successfully: {}", addedTrainer);
        return new ResponseEntity<>(addedTrainer,HttpStatus.valueOf(201));
    }

    @GetMapping("/api/trainer/{trainerId}")
    public ResponseEntity<Trainer>getTrainerById(@PathVariable Long trainerId){
        logger.info("Request to get trainer by ID: {}", trainerId);
        Optional<Trainer>trainer=trainerService.getTrainerById(trainerId);
        logger.info("Trainer details retrieved: {}", trainer);
        return new ResponseEntity<>(trainer.get(),HttpStatus.valueOf(200));

    }

    @GetMapping("/api/trainer")
    public ResponseEntity<List<Trainer>>getAllTrainers(){
        logger.info("Request to get all trainers");
        logger.info("Fetching all trainers from the database");
        List<Trainer>trainers=trainerService.getAllTrainers();
        if(trainers.isEmpty()){
            logger.warn("No trainers found in the database");
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        logger.info("Total trainers retrieved: {}", trainers.size());
        return new ResponseEntity<>(trainers,HttpStatus.valueOf(200));
    }


    @PutMapping("/api/trainer/{trainerId}")
    public ResponseEntity<Trainer>updateTrainer(@PathVariable Long trainerId,@RequestBody Trainer trainer){
        logger.info("Request to update trainer with ID: {}", trainerId);
        Trainer updatedTrainer=trainerService.updateTrainer(trainerId, trainer);
        logger.info("Trainer updated successfully: {}", updatedTrainer);
        return new ResponseEntity<>(updatedTrainer,HttpStatusCode.valueOf(200));
     
    }


    @DeleteMapping("/api/trainer/{trainerId}")
    public ResponseEntity<Trainer>deleteTrainer(@PathVariable Long trainerId){
        logger.info("Request to delete trainer with ID: {}", trainerId);
        Trainer deletedTrainer=trainerService.deleteTrainer(trainerId);
        logger.info("Trainer deleted successfully: {}", deletedTrainer);
        return new ResponseEntity<>(deletedTrainer,HttpStatusCode.valueOf(200));
    }

    @GetMapping("/api/trainers")
    public Page<Trainer> getTrainersByPage(@RequestParam( defaultValue = "0") int page,@RequestParam( defaultValue = "10") int size)
    {
        logger.info("Fetching trainers for page: {} and size: {}", page, size);
        Page<Trainer> trainerPage=trainerService.getTrainersByPage(page, size);
        if(trainerPage.isEmpty())
        {
            logger.warn("No trainers found for page: {} and size: {}", page, size);
            return null;
        }
        logger.info("Trainers fetched successfully for page: {} and size: {}", page, size);
        return trainerPage;
    }

    
}
