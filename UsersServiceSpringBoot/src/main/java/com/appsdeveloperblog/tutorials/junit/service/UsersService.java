package com.appsdeveloperblog.tutorials.junit.service;

import com.appsdeveloperblog.tutorials.junit.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UsersService extends UserDetailsService {
    UserDto createUser(UserDto user);
    List<UserDto> getUsers(int page, int limit);
    UserDto getUser(String email);
}
