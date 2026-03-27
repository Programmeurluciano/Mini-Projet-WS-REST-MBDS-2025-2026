package com.miniprojet.wsrest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MovieRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotNull(message = "L'année est obligatoire")
    @Min(value = 1888, message = "L'année doit être supérieure à 1888")
    private Integer year;

    private String posterUrl;
    private String trailerUrl;

    private List<Long> actorIds;
    private List<Long> categoryIds;
}