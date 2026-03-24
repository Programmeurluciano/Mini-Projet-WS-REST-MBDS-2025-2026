package com.miniprojet.wsrest.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    private Instant expiryDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
    
}
