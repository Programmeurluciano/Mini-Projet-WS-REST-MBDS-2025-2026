package com.miniprojet.wsrest.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthenticationRequest {
    private String email;
    private String password;
}