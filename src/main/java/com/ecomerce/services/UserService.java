package com.ecomerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecomerce.entities.User;
import com.ecomerce.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

   
    public User getAuthenticatedUser() {
        // Pega o UserDetails a partir do contexto de segurança
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Agora use o repositório para pegar o objeto User real baseado no username
        User user = (User) userRepository.findByUsername(username);  // Cast ou conversão para User
        return user;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        User user = getAuthenticatedUser();
        
        // Verifica se a senha antiga está correta
        if (passwordEncoder.matches(oldPassword, user.getPassword())) {
            // Atualiza com a nova senha criptografada
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }
    }
    

