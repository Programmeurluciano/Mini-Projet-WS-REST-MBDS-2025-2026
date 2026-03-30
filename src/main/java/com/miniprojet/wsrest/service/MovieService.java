package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.dto.MovieMapper;
import com.miniprojet.wsrest.dto.MovieRequestDTO;
import com.miniprojet.wsrest.dto.MovieResponseDTO;
import com.miniprojet.wsrest.model.Actor;
import com.miniprojet.wsrest.model.Category;
import com.miniprojet.wsrest.model.Movie;
import com.miniprojet.wsrest.repository.ActorRepository;
import com.miniprojet.wsrest.repository.CategoryRepository;
import com.miniprojet.wsrest.repository.MovieRepository;
import com.miniprojet.wsrest.specification.MovieSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;
    private final CategoryRepository categoryRepository;

    public MovieService(MovieRepository movieRepository,
                        ActorRepository actorRepository,
                        CategoryRepository categoryRepository) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<MovieResponseDTO> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(MovieMapper::toResponse);
    }

    public Optional<MovieResponseDTO> getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(movie -> {
                    MovieResponseDTO dto = MovieMapper.toResponse(movie);
                    dto.setAverageRating(getAverageRating(id));
                    return dto;
                });
    }

    public MovieResponseDTO createMovie(MovieRequestDTO dto) {
        Movie movie = MovieMapper.toEntity(dto);

        if (dto.getActorIds() != null && !dto.getActorIds().isEmpty()) {
            List<Actor> actors = actorRepository.findAllById(dto.getActorIds());
            movie.setActors(actors);
        } else {
            movie.setActors(Collections.emptyList());
        }

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            movie.setCategories(categories);
        } else {
            movie.setCategories(Collections.emptyList());
        }

        return MovieMapper.toResponse(movieRepository.save(movie));
    }

    public MovieResponseDTO updateMovie(Long id, MovieRequestDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + id));

        movie.setTitle(dto.getTitle());
        movie.setYear(dto.getYear());
        movie.setPosterUrl(dto.getPosterUrl());
        movie.setTrailerUrl(dto.getTrailerUrl());

        if (dto.getActorIds() != null) {
            List<Actor> actors = actorRepository.findAllById(dto.getActorIds());
            movie.setActors(actors);
        }

        if (dto.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
            movie.setCategories(categories);
        }

        return MovieMapper.toResponse(movieRepository.save(movie));
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Page<MovieResponseDTO> getMoviesByYear(int year, Pageable pageable) {
        return movieRepository.findByYear(year, pageable)
                .map(MovieMapper::toResponse);
    }

    public Page<MovieResponseDTO> getMoviesByActor(String actorName, Pageable pageable) {
        return movieRepository.findByActors_Name(actorName, pageable)
                .map(MovieMapper::toResponse);
    }

    public Page<MovieResponseDTO> getMoviesByCategory(String categoryName, Pageable pageable) {
        return movieRepository.findByCategories_Name(categoryName, pageable)
                .map(MovieMapper::toResponse);
    }

    public Double getAverageRating(Long movieId) {
        Double avg = movieRepository.getAverageRating(movieId);
        return avg != null ? avg : 0.0;
    }

    public Page<MovieResponseDTO> getTopRatedMovies(Pageable pageable) {
        return movieRepository.findTopRatedMovies(pageable)
                .map(MovieMapper::toResponse);
    }

    public Page<MovieResponseDTO> searchMovies(String title, Integer year,
                                               String actor, String category, Pageable pageable) {

        Specification<Movie> spec = (root, query, cb) -> cb.conjunction();

        if (title != null)    spec = spec.and(MovieSpecification.titleContains(title));
        if (year != null)     spec = spec.and(MovieSpecification.hasYear(year));
        if (actor != null)    spec = spec.and(MovieSpecification.hasActor(actor));
        if (category != null) spec = spec.and(MovieSpecification.hasCategory(category));

        return movieRepository.findAll(spec, pageable)
                .map(MovieMapper::toResponse);
    }

    public MovieResponseDTO uploadMedia(Long movieId, MultipartFile image,
                                        MultipartFile video) throws Exception {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if (image != null && !image.isEmpty()) {
            String imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get("src/main/resources/static/uploads/images/" + imageName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());
            movie.setPosterUrl("/uploads/images/" + imageName);
        }

        if (video != null && !video.isEmpty()) {
            String videoName = System.currentTimeMillis() + "_" + video.getOriginalFilename();
            Path videoPath = Paths.get("src/main/resources/static/uploads/videos/" + videoName);
            Files.createDirectories(videoPath.getParent());
            Files.write(videoPath, video.getBytes());
            movie.setTrailerUrl("/uploads/videos/" + videoName);
        }

        return MovieMapper.toResponse(movieRepository.save(movie));
    }
}