package com.ecomerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SuppressWarnings("unused")
@SpringBootApplication
@EnableAutoConfiguration //(exclude = { SecurityAutoConfiguration. class }) 
public class StoreAplication {

	public static void main(String[] args) {
		
		SpringApplication.run(StoreAplication.class, args);
	}

    }
	

