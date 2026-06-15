package com.examly.springapp.controller;

import java.util.List;

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

import com.examly.springapp.model.Requirement;
import com.examly.springapp.model.RequirementRequestDto;
import com.examly.springapp.service.RequirementService;

@RestController
public class RequirementController 
{

    private static final Logger logger=LoggerFactory.getLogger(RequirementController.class);
    
    private RequirementService requirementService;
    
    public RequirementController(RequirementService requirementService){
        this.requirementService=requirementService;
    }

    @PostMapping("/api/requirement")
    public ResponseEntity<Requirement> addRequirement(@RequestBody Requirement requirement) {
    logger.info("Received request to add requirement: {}", requirement);
    Requirement savedRequirement = requirementService.addRequirement(requirement);
    logger.info("Requirement added successfully: {}", savedRequirement);
    logger.info("Requirement details: ID={}, Title={}, Description={}, Trainer ID={}", savedRequirement.getRequirementId(), savedRequirement.getTitle(), savedRequirement.getDescription(), savedRequirement.getTrainerId());
    return new ResponseEntity<>(savedRequirement,HttpStatus.valueOf(201));
    }

@GetMapping("/api/requirement/{requirementId}")
public ResponseEntity<Requirement> getRequirementById(@PathVariable long requirementId){
    logger.info("Request to get requirement by ID: {}", requirementId);

    Requirement requirementById=requirementService.getRequirementById(requirementId).get();
    logger.info("Requirement details retrieved: ID={}, Title={}, Description={}, Trainer ID={}", requirementById.getRequirementId(), requirementById.getTitle(), requirementById.getDescription(), requirementById.getTrainerId());
    return new ResponseEntity<>(requirementById,HttpStatusCode.valueOf(200));
    
}

@GetMapping("/api/requirement")
public ResponseEntity<List<Requirement>> getAllRequirements(){

    logger.info("Request to get all requirements");
    logger.info("Fetching all requirements from the database");
    List<Requirement> requirements = requirementService.getAllRequirements(); 
    if(requirements.isEmpty()){
        logger.warn("No requirements found in the database");
        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
    }
    logger.info("Total requirements retrieved: {}", requirements.size());
    return new ResponseEntity<>(requirements,HttpStatusCode.valueOf(200));   
    
}

@PutMapping("/api/requirement/{requirementId}")
public ResponseEntity<Requirement> updateRequirement(@PathVariable long requirementId,@RequestBody RequirementRequestDto requirementRequestDto){
    logger.info("Request to update requirement with ID: {}", requirementId);
    logger.info("Requirement update details: {}", requirementRequestDto);
    Requirement updatedRequirement = requirementService.updateRequirement(requirementId, requirementRequestDto);
    logger.info("Requirement updated successfully: ID={}, Title={}, Description={}, Trainer ID={}", updatedRequirement.getRequirementId(), updatedRequirement.getTitle(), updatedRequirement.getDescription(), updatedRequirement.getTrainerId());
    return new ResponseEntity<>(updatedRequirement,HttpStatusCode.valueOf(200));
}

@DeleteMapping("/api/requirement/{requirementId}")
public ResponseEntity<Requirement> deleteRequirement(@PathVariable long requirementId){
    logger.info("Request to delete requirement with ID: {}", requirementId);
    Requirement deletedRequirement = requirementService.deleteRequirement(requirementId);
    logger.info("Requirement deleted successfully: ID={}, Title={}, Description={}, Trainer ID={}", deletedRequirement.getRequirementId(), deletedRequirement.getTitle(), deletedRequirement.getDescription(), deletedRequirement.getTrainerId());
    return new ResponseEntity<>(deletedRequirement,HttpStatusCode.valueOf(200));
}

@GetMapping("/api/requirement/trainer/{trainerId}")
public ResponseEntity<List<Requirement>> getRequirementsByTrainerId(@PathVariable long trainerId){
    logger.info("Request to get requirements by Trainer ID: {}", trainerId);
    List<Requirement> requirementById = requirementService.getRequirementsByTrainerId(trainerId);
    logger.info("Requirements retrieved for Trainer ID {}: {}", trainerId, requirementById.size());
    return new ResponseEntity<>(requirementById,HttpStatusCode.valueOf(200));
}

@GetMapping("/api/requirements/page")
public Page<Requirement>  getReqPage(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size)
{
    logger.info("Fetching requirements for page: {} and size: {}", page, size);
    Page<Requirement> requirementsPage=requirementService.getRequirementsByPages(page, size);
    if(requirementsPage.isEmpty()){
        logger.warn("No requirements found for page: {} and size: {}", page, size);
        return null;
    }
    logger.info("Requirements fetched successfully for page: {} and size: {}", page, size);
    return requirementsPage;
}


@GetMapping("/api/requirements")
public Page<Requirement>  getRequirementsByPage(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size)
{

    logger.info("Fetching requirements for page: " + page + ", size: " + size);

    Page<Requirement> requirementsPage=requirementService.getRequirementsByPages(page, size);
    if(requirementsPage.isEmpty()){
        return null;
    }
    logger.info("Requirements fetched successfully for page: " + page + ", size: " + size);
    logger.info("Total requirements found: " + requirementsPage.getTotalElements());
    logger.info("Total pages available: " + requirementsPage.getTotalPages());

    return requirementsPage;
}
                


}
