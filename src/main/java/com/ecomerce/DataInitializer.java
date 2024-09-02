package com.ecomerce;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.ecomerce.entities.Sale;
import com.ecomerce.entities.Product;
import com.ecomerce.repositories.ProductRepository;
import com.ecomerce.repositories.SaleRepository;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

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
            p.setPreco((double) (i * 50 + 50)); // Exemplo: Preço entre 100 e 1050
            p.setStatus("INATIVO");
            products.add(p);
        }
        productRepository.saveAll(products);

        // Criando 10 vendas aleatorias
        Random random = new Random();
        List<Sale> sales = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Sale s = new Sale();

            // Gerando uma data aleatória em 2023
            int day = random.nextInt(1, 29); 
            int month = random.nextInt(1, 13);
            int hour = random.nextInt(0, 24);
            int minute = random.nextInt(0, 60);

            LocalDateTime randomDate = LocalDateTime.of(2023, Month.of(month), day, hour, minute);
            s.setDate(randomDate);

            // Adicionando os primeiros 2 produtos de cada venda
            s.getProducts().add(products.get(i * 2));
            s.getProducts().add(products.get(i * 2 + 1));

            sales.add(s);
        }
        saleRepository.saveAll(sales);
    }
}