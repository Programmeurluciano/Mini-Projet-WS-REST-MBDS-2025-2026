package com.miniprojet.wsrest.dto;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
}