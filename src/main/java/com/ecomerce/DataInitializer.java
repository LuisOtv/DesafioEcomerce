package com.ecomerce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.ecomerce.entities.Product;
import com.ecomerce.repositories.ProductRepository;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        // Lista de nomes de aparelhos eletrônicos
        String[] eletronicDevices = {
            "Smartphone", "Laptop", "Tablet", "Smartwatch", "Television",
            "Bluetooth Speaker", "Headphones", "Gaming Console", "Camera", "Printer",
            "Router", "Smart Home Hub", "Drone", "External Hard Drive", "E-Reader",
            "Wireless Charger", "Smart Light Bulb", "VR Headset", "Soundbar", "Streaming Stick"
        };

        // Criando 20 produtos aleatorios
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < eletronicDevices.length; i++) {
            Product p = new Product();
            p.setNome(eletronicDevices[i]);
            p.setPreco((double) (i * 50 + 50));
            p.setStatus("DISPONIVEL");
            products.add(p);
        }
        productRepository.saveAll(products);
       
    }
}