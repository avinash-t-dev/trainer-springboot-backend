package com.examly.springapp.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    private long userId;
    private String username;
    private String email;
    private String mobileNumber;

}
