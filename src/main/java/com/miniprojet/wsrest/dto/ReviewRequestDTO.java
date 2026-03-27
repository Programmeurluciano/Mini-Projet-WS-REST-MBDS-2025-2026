package com.miniprojet.wsrest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewRequestDTO {

    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note minimale est 1")
    @Max(value = 10, message = "La note maximale est 10")
    private Integer rating;

    private String comment;

    @NotNull(message = "L'ID du film est obligatoire")
    private Long movieId;
}