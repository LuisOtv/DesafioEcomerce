package com.ecomerce.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomerce.entities.Product;
import com.ecomerce.repositories.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product addProduct(Product product) {

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }

        return productRepository.save(product);
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public Product putProduct(Long id, Product productAtualizado) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productAtualizado.getName());
                    product.setPrice(productAtualizado.getPrice());
                    product.setStatus(productAtualizado.getStatus());
                    return productRepository.save(product);
                }).orElseThrow(() -> new RuntimeException("Product não encontrado"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
