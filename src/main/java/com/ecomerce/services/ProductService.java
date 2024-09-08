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

    public Product addProduct(Product Product) {
        return productRepository.save(Product);
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProduct(Long id) {
        return productRepository.findById(id);
    }

    public Product putProduct(Long id, Product ProductAtualizado) {
        return productRepository.findById(id)
                .map(Product -> {
                    Product.setName(ProductAtualizado.getName());
                    Product.setPrice(ProductAtualizado.getPrice());
                    Product.setStatus(ProductAtualizado.getStatus());
                    return productRepository.save(Product);
                }).orElseThrow(() -> new RuntimeException("Product não encontrado"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
