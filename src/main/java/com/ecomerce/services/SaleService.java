package com.ecomerce.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecomerce.entities.Product;
import com.ecomerce.entities.Sale;
import com.ecomerce.entities.User;
import com.ecomerce.repositories.ProductRepository;
import com.ecomerce.repositories.SaleRepository;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Sale> listSales() {
        // Obter o CustomUserDetails a partir do SecurityContextHolder
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Obter o userId diretamente do UserDetails
        Long userId = user.getId();

        // Buscar as vendas associadas ao userId
        return saleRepository.findByUserId(userId);
    }

    public void confirmSale(List<Long> saleIds) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for (Long saleId : saleIds) {
            Sale sale = saleRepository.findById(saleId)
                    .orElseThrow(() -> new RuntimeException("Sale não encontrada: " + saleId));

            // Verifica se o usuário autenticado é o mesmo que fez a venda
            if (!user.getId().equals(sale.getUser().getId())) {
                throw new RuntimeException("Você não tem permissão para terminar esta venda: " + saleId);
            }

            // Tornar todos os produtos da venda "DISPONÍVEL"
            for (Product product : sale.getProducts()) {
                product.setStatus("VENDIDO");
                productRepository.save(product);
            }

            // Deletar a venda
            saleRepository.delete(sale);
        }

    }

    public void cancelSale(List<Long> saleIds) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for (Long saleId : saleIds) {
            Sale sale = saleRepository.findById(saleId)
                    .orElseThrow(() -> new RuntimeException("Sale não encontrada: " + saleId));

            // Verifica se o usuário autenticado é o mesmo que fez a venda
            if (!user.getId().equals(sale.getUser().getId())) {
                throw new RuntimeException("Você não tem permissão para cancelar esta venda: " + saleId);
            }

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

        // Obter o CustomUserDetails a partir do SecurityContextHolder
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Se o ID da venda for fornecido, buscar a venda existente, caso contrário criar uma nova
        if (saleId.isPresent()) {
            sale = saleRepository.findById(saleId.get())
                    .orElseThrow(() -> new RuntimeException("Sale não encontrada"));
        } else {
            sale = new Sale();
            sale.setUser(user);
            sale.setDate(LocalDateTime.now());
        }

        // Primeiro, verificar se algum produto está inativo ou vendido
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product não encontrado"));

            if ("INATIVO".equals(product.getStatus()) || "VENDIDO".equals(product.getStatus())) {
                throw new RuntimeException("Product " + product.getName() + " já foi vendido e está inativo.");
            }
        }

        // Se todos os produtos estiverem ativos, realizar as mudanças
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product não encontrado"));

            product.setStatus("INATIVO"); // Tornando o produto inativo após ser adicionado à venda
            productRepository.save(product);

            sale.getProducts().add(product);
        }

        return saleRepository.save(sale);
    }

}
