package com.ecomerce;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

        try (Scanner sc = new Scanner(System.in)) {

            System.out.println("APERTE '1' PARA POPULAR A TABELA PRODUTOS");
            if (sc.nextInt() == 1) {

                String[] eletronicDevices = {
                    "Smartphone", "Laptop", "Tablet", "Smartwatch", "Television",
                    "Bluetooth Speaker", "Headphones", "Gaming Console", "Camera", "Printer",
                    "Router", "Smart Home Hub", "Drone", "External Hard Drive", "E-Reader",
                    "Wireless Charger", "Smart Light Bulb", "VR Headset", "Soundbar", "Streaming Stick"
                };

                List<Product> products = new ArrayList<>();
                for (int i = 0; i < eletronicDevices.length; i++) {
                    Product p = new Product();
                    p.setName(eletronicDevices[i]);
                    p.setPrice((double) (i * 50 + 50));
                    p.setStatus("DISPONíVEL");
                    products.add(p);
                }
                productRepository.saveAll(products);

                System.out.println("POPULADO");
            }

        }

    }
}
