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
    private ProductRepository ProductRepository;

    public Product addProduct(Product Product) {
        return ProductRepository.save(Product);
    }

    public List<Product> listProducts() {
        return ProductRepository.findAll();
    }

    public Optional<Product> getProduct(Long id) {
        return ProductRepository.findById(id);
    }

    public Product putProduct(Long id, Product ProductAtualizado) {
        return ProductRepository.findById(id)
            .map(Product -> {
                Product.setNome(ProductAtualizado.getNome());
                Product.setPreco(ProductAtualizado.getPreco());
                Product.setEstoque(ProductAtualizado.getEstoque());
                Product.setStatus(ProductAtualizado.getStatus());
                return ProductRepository.save(Product);
            }).orElseThrow(() -> new RuntimeException("Product não encontrado"));
    }

    public void deleteProduct(Long id) {
        ProductRepository.deleteById(id);
    }
}