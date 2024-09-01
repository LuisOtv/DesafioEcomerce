package com.ecomerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomerce.entities.Product;
import com.ecomerce.entities.Sale;
import com.ecomerce.repositories.ProductRepository;
import com.ecomerce.repositories.SaleRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Sale> listSales() {
        return saleRepository.findAll();
    }

    public void confirmSale(List<Long> saleIds) {
        for (Long saleId : saleIds) {
            Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale não encontrada: " + saleId));

             // Remover a relação entre a venda e os produtos
        for (Product product : sale.getProducts()) {
            product.setStatus("VENDIDO");;
            productRepository.save(product); // Salvar as alterações na lista de vendas do produto
        }

        // Deletar a venda
        saleRepository.delete(sale);
    }
    }

    public void cancelSale(List<Long> saleIds) {
        for (Long saleId : saleIds) {
            Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new RuntimeException("Sale não encontrada: " + saleId));

            // Tornar todos os produtos da venda "DISPONÍVEL"
            for (Product product : sale.getProducts()) {
                product.setStatus("DISPONÍVEL");
                productRepository.save(product);
            }

            // Deletar a venda
            saleRepository.delete(sale);
        }
    }
    public Sale addProduct(List<Long> productIds, Optional<Long> saleId) {
        Sale sale;

        // Se o ID da venda for fornecido, buscar a venda existente, caso contrário criar uma nova
        if (saleId.isPresent()) {
            sale = saleRepository.findById(saleId.get())
                .orElseThrow(() -> new RuntimeException("Sale não encontrada"));
        } else {
            sale = new Sale();
            sale.setDate(LocalDateTime.now());
        }

        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product não encontrado"));

            if ("INATIVO".equals(product.getStatus()) || "VENDIDO".equals(product.getStatus())) {
                throw new RuntimeException("Product " + product.getNome() + " já foi vendido e está inativo.");
            }

            product.setStatus("INATIVO"); // Tornando o produto inativo após ser adicionado à venda
            productRepository.save(product);

            sale.getProducts().add(product);
        }

        return saleRepository.save(sale);
    }

}
