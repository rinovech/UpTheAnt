package com.uptheant.demo.service.user;

import com.uptheant.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(Integer id);
    User createUser(User user);
    void deleteUser(Integer id);
}