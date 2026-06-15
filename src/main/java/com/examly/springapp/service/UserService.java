
package com.examly.springapp.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.examly.springapp.model.User;
import com.examly.springapp.model.UserResponseDto;

@Service
public interface UserService {

    User createUser(User user);
    User loginUser(User user);
    UserResponseDto getUserById(long userId);
    List<UserResponseDto> getAllUsers();
    UserResponseDto addUser(User user);

}