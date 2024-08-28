package com.example.service;

import com.example.web.dto.UserDto;
import com.example.web.payload.CreateUserRequest;

import java.util.List;

public interface UserService {

    void createUser(CreateUserRequest createUserRequest);

    UserDto getUser(Long userId);

    void deleteUser(Long userId);

    List<UserDto> getUsers();

}
