package com.miniprojet.wsrest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;
}