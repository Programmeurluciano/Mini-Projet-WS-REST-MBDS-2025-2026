package com.miniprojet.wsrest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MovieResponseDTO {

    private Long id;
    private String title;
    private int year;
    private String posterUrl;
    private String trailerUrl;

    private List<String> actors;
    private List<String> categories;
    private Double averageRating;
}
