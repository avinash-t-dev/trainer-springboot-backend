package com.examly.springapp.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.examly.springapp.exceptions.PasswordMismatchException;
import com.examly.springapp.exceptions.UserNotFoundException;
import com.examly.springapp.model.User;
import com.examly.springapp.model.UserResponseDto;
import com.examly.springapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepo;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public UserResponseDto addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return new UserResponseDto(user.getUserId(), user.getUsername(), user.getEmail(), user.getMobileNumber());
    }

    
    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public User loginUser(User user) {
        User existingUser=userRepo.findByEmail(user.getEmail()).orElse(null);
        if(existingUser==null){
            throw new UserNotFoundException("User Not Found with email ,Kindly register to proceed");
        }
        if(!(passwordEncoder.matches(user.getPassword(), existingUser.getPassword()))){
            throw new PasswordMismatchException("Password is incorrect.. please enter valid password");
        }
      return existingUser;
    }


    @Override
    public UserResponseDto getUserById(long userId) {

        User user=userRepo.findById(userId).orElseThrow(()->new UserNotFoundException("User Not Found"));
        UserResponseDto userRequestDto=new UserResponseDto(user.getUserId(),user.getUsername(),user.getEmail(),user.getMobileNumber());
        return userRequestDto;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepo.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return users.stream()
                .map(user -> new UserResponseDto(user.getUserId(),user.getUsername(), user.getEmail(), user.getMobileNumber()))
                .toList();
        
    }

}
