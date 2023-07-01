package com.surja.urlshortner.controller;

import com.surja.urlshortner.payload.UserDto;
import com.surja.urlshortner.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/get-user")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
        UserDto userDetails = this.userService.getUserDetails(request);
        return ResponseEntity.ok(userDetails);
    }
}
