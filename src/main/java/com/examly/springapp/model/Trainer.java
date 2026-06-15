package com.examly.springapp.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainerId;
    private String name;
    private String email;
    private String phone;
    private String expertise;
    private String experience;
    private String certification;
    @Lob
    private String resume;
    private LocalDate joiningDate;
    private String status;

    @OneToMany(mappedBy = "trainer",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Feedback> feedbacks=new ArrayList<>();
    
    @OneToMany(mappedBy = "trainer",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Requirement> requirements=new ArrayList<>();
    
}

