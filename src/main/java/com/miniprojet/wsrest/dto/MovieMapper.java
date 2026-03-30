package com.miniprojet.wsrest.dto;


import com.miniprojet.wsrest.model.Actor;
import com.miniprojet.wsrest.model.Category;
import com.miniprojet.wsrest.model.Movie;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MovieMapper {

    public static MovieResponseDTO toResponse(Movie movie) {
        MovieResponseDTO dto = new MovieResponseDTO();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setYear(movie.getYear());
        dto.setPosterUrl(movie.getPosterUrl());
        dto.setTrailerUrl(movie.getTrailerUrl());

        List<String> actorNames = movie.getActors() != null
                ? movie.getActors().stream()
                .map(Actor::getName)
                .collect(Collectors.toList())
                : Collections.emptyList();
        dto.setActors(actorNames);

        List<String> categoryNames = movie.getCategories() != null
                ? movie.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList())
                : Collections.emptyList();
        dto.setCategories(categoryNames);

        dto.setAverageRating(0.0);

        return dto;
    }

    public static Movie toEntity(MovieRequestDTO dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setYear(dto.getYear());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setTrailerUrl(dto.getTrailerUrl());
        return movie;
    }
}