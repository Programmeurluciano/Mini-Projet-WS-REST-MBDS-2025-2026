package com.miniprojet.wsrest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewResponseDTO {

    private Long id;
    private int rating;
    private String comment;

    private Long movieId;
    private String movieTitle;
    private Long userId;
    private String userFullname;
}