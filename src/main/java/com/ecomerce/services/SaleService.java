package com.ecomerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomerce.entities.Product;
import com.ecomerce.entities.Sale;
import com.ecomerce.repositories.ProductRepository;
import com.ecomerce.repositories.SaleRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    public Sale addProduct(List<Long> productIds, Long saleId) {
        Sale sale;
        
        if (saleId == null) {
            // Cria uma nova Sale se o saleId não for fornecido
            sale = new Sale();
        } else {
            sale = saleRepository.findById(saleId)
                .orElseGet(() -> new Sale()); // Cria uma nova Sale se o saleId for inválido
        }

        List<Product> produtos = productRepository.findAllById(productIds);
        sale.getProducts().addAll(produtos);
        
        return saleRepository.save(sale);
    }
}
