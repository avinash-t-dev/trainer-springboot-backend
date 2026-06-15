package com.examly.springapp.model;

import lombok.NoArgsConstructor;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LoginDTO {
    
    private String token;
    private String username;
    private String userRole;
    private Long userId;

}