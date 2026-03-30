package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.dto.ReviewMapper;
import com.miniprojet.wsrest.dto.ReviewRequestDTO;
import com.miniprojet.wsrest.dto.ReviewResponseDTO;
import com.miniprojet.wsrest.model.Movie;
import com.miniprojet.wsrest.model.Review;
import com.miniprojet.wsrest.model.User;
import com.miniprojet.wsrest.repository.MovieRepository;
import com.miniprojet.wsrest.repository.ReviewRepository;
import com.miniprojet.wsrest.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         MovieRepository movieRepository,
                         UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    public Page<ReviewResponseDTO> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(ReviewMapper::toResponse);
    }

    public Optional<ReviewResponseDTO> getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(ReviewMapper::toResponse);
    }

    public ReviewResponseDTO createReview(ReviewRequestDTO dto, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + dto.getMovieId()));

        Review review = new Review();
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setUser(user);
        review.setMovie(movie);

        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    public ReviewResponseDTO updateReview(Long id, ReviewRequestDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id " + id));

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Page<ReviewResponseDTO> getReviewsByMovie(Long movieId, Pageable pageable) {
        return reviewRepository.findByMovieId(movieId, pageable)
                .map(ReviewMapper::toResponse);
    }

    public Page<ReviewResponseDTO> getReviewsByUser(Long userId, Pageable pageable) {
        return reviewRepository.findByUserId(userId, pageable)
                .map(ReviewMapper::toResponse);
    }
}
