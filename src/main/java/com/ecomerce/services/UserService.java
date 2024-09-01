package com.ecomerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomerce.entities.User;
import com.ecomerce.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User putUser(Long id, User userAtualizado) {
        return userRepository.findById(id)
            .map(user -> {
                user.setUsername(userAtualizado.getUsername());
                user.setPassword(userAtualizado.getPassword());
                return userRepository.save(user);
            }).orElseThrow(() -> new RuntimeException("User não encontrado"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
