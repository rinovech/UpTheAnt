package com.UpTheAnt.demo.service;

import com.UpTheAnt.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private Integer nextUserId = 1;

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(Integer id) {
        return users.stream()
                .filter(user -> user.getUserId().equals(id))
                .findFirst();
    }

    public User createUser(User user) {
        user.setUserId(nextUserId++);
        users.add(user);
        return user;
    }

    public void deleteUser(Integer id) {
        users.removeIf(user -> user.getUserId().equals(id));
    }
}