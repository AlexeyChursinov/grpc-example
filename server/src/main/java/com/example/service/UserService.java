package com.example.service;

import com.example.model.User;

import java.util.List;

public interface UserService {

    void createUser(User user);

    User getUserById(long userId);

    void deleteUser(long userId);

    List<User> getUsers();
}
