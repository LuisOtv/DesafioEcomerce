package com.ecomerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.dto.AuthenticationDTO;
import com.ecomerce.dto.RegisterDTO;
import com.ecomerce.entities.User;
import com.ecomerce.repositories.UserRepository;
import com.ecomerce.security.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenService tokenService;

    // Logar Uusuario
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok("Usuário logado com sucesso!\nToken de Acesso: " + token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro: " + e.getMessage());
        }
    }

    // Registrar Usuario
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.findByUsername(data.username()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Nome de usuário já existente.");
        
        }else if (data.username().length() < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Nome de usuário deve ter mais de 3 caracteres.");
        }

        if (data.password().length() < 8) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: A senha deve ter no mínimo 8 caracteres.");
        }

        try {
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = new User(data.username(), encryptedPassword, data.role());

            this.userRepository.save(newUser);

            return ResponseEntity.ok("Usuário criado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " + e.getMessage());
        }
    }
}
