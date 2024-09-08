package com.ecomerce.dto;

import com.ecomerce.entities.UserRole;

public record RegisterDTO(String username, String password, UserRole role) {

}
