package com.surja.urlshortner.services;

import com.surja.urlshortner.payload.UserDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    public UserDto getUserDetails(HttpServletRequest request);
    public UserDto createUser(UserDto userDto);
}
