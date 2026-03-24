package com.miniprojet.wsrest.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    private String fullname;
    private String email;
    private String password;
}