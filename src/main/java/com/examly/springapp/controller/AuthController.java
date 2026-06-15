package com.examly.springapp.controller;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.config.MyUserDetailsService;
import com.examly.springapp.exceptions.UserNotFoundException;
import com.examly.springapp.model.ApiResponseDto;
import com.examly.springapp.model.LoginRequestDto;
import com.examly.springapp.model.LoginResponseDto;
import com.examly.springapp.model.User;
import com.examly.springapp.model.UserResponseDto;
import com.examly.springapp.repository.UserRepository;
import com.examly.springapp.service.UserService;


@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger logger=LoggerFactory.getLogger(AuthController.class);

    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private MyUserDetailsService userDetailsService;
    private UserRepository userRepository;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtils jwtUtils, MyUserDetailsService userDetailsService,UserRepository userRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.userRepository=userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> createUser(@RequestBody User user) {
        logger.info("Received registration request for user: {}", user.getEmail());
        UserResponseDto newUser = userService.addUser(user);
        if(newUser != null) {
            logger.info("User registered successfully: {}", newUser.getEmail());
            logger.info("User details: ID={}, Username={}, Email={}, Mobile={}", newUser.getUserId(), newUser.getUsername(), newUser.getEmail(), newUser.getMobileNumber());
            return new ResponseEntity<ApiResponseDto<UserResponseDto>>(new ApiResponseDto<UserResponseDto>(true, "Registered Successfully",newUser ), HttpStatus.CREATED);
        }
        logger.error("Registration failed for user: {}", user.getEmail());
        return new ResponseEntity<ApiResponseDto<UserResponseDto>>(new ApiResponseDto<UserResponseDto>(false, "Error occured,Registration Unsuccessful..", null), HttpStatus.BAD_REQUEST);
         
    } 


    @PostMapping("/login")
    public ResponseEntity<?> loginTest(@RequestBody LoginRequestDto loginRequestDto) {
        logger.info("Login attempt for user: {}", loginRequestDto.getEmail());
        try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

         User user=userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(()-> new UserNotFoundException("User Doesnt exist with this email , kindly register"+loginRequestDto.getEmail()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getEmail());
        String token = jwtUtils.generateToken(userDetails,user.getUsername(),user.getEmail(),user.getUserRole(),user.getUserId());

        LoginResponseDto response = new LoginResponseDto(
            token,
            user.getUsername(),
            user.getUserId(),
            userDetails.getAuthorities().toString()
        );

        logger.info("Login successful for user: {}", user.getUsername());
        logger.info("Generated token: {}", token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
        logger.error("Login failed for user: {}. Error: {}", loginRequestDto.getEmail(), e.getMessage());
        return new ResponseEntity<>(new ApiResponseDto<>(false, "Login failed: " + e.getMessage(), null), HttpStatus.UNAUTHORIZED);
    }
}

@GetMapping("/user/{userId}")
public ResponseEntity<UserResponseDto> getUserById(@PathVariable long userId)
{
    logger.info("Request to get user by ID: {}", userId);
    UserResponseDto response=userService.getUserById(userId);
    logger.info("User details retrieved: ID={}, Username={}, Email={}, Mobile={}", response.getUserId(), response.getUsername(), response.getEmail(), response.getMobileNumber());
    return new ResponseEntity<UserResponseDto>(response,HttpStatus.valueOf(200));
    
}

@GetMapping("/users")
public ResponseEntity<List<UserResponseDto>> getAllUsers()
{
    logger.info("Request to get all users");
    List<UserResponseDto> users=userService.getAllUsers();
    logger.info("Total users retrieved: {}", users.size());
    return new ResponseEntity<List<UserResponseDto>>(users,HttpStatus.valueOf(200));
}


}


