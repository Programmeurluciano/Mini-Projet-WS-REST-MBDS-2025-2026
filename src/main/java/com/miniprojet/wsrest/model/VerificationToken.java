package com.miniprojet.wsrest.model;



import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VerificationToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private Instant expiryDate;

}
