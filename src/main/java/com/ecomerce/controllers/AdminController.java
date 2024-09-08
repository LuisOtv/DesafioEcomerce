package com.ecomerce.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomerce.dto.RegisterDTO;
import com.ecomerce.entities.Product;
import com.ecomerce.entities.User;
import com.ecomerce.repositories.UserRepository;
import com.ecomerce.services.ProductService;

import jakarta.validation.Valid;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    // Deletar produto se possível
    @DeleteMapping("delete-product/{id}")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        Optional<Product> optionalProduct = productService.getProduct(id);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erro: Produto com ID " + id + " não encontrado.");
        }

        Product product = optionalProduct.get();
        if (!"DISPONÍVEL".equalsIgnoreCase(product.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: Produto não está disponível para exclusão.");
        }

        productService.deleteProduct(id);
        return ResponseEntity.ok("Produto com ID " + id + " deletado com sucesso.");
    }

    // Criar Produtos
    @PostMapping("/add-product")
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        try {
            Product novoProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Produto criado com sucesso: " + novoProduct.getName());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro: " + e.getMessage());
        }
    }

    // Criar Usuarios
    @PostMapping("/add-user")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.findByUsername(data.username()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: Nome de usuário já existente.");
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
