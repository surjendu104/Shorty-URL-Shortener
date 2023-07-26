package com.surja.urlshortner.controller;

import com.surja.urlshortner.payload.ApiResponse;
import com.surja.urlshortner.payload.UserDto;
import com.surja.urlshortner.security.JwtHelper;
import com.surja.urlshortner.security.JwtRequest;
import com.surja.urlshortner.security.JwtResponse;
import com.surja.urlshortner.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper helper;

    @CrossOrigin(origins = "*")
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .username(userDetails.getUsername())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(BadCredentialsException e) {
        String message = e.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,true,400);
        return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        UserDto newUser = this.userService.createUser(userDto);
        return new ResponseEntity<>(newUser,HttpStatus.OK);
    }
}
