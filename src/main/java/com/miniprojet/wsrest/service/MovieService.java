package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.model.Movie;
import com.miniprojet.wsrest.repository.MovieRepository;
import com.miniprojet.wsrest.specification.MovieSpecification;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        movie.setTitle(updatedMovie.getTitle());
        movie.setYear(updatedMovie.getYear());
        movie.setActors(updatedMovie.getActors());
        movie.setCategories(updatedMovie.getCategories());

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public Page<Movie> getMoviesByYear(int year, Pageable pageable) {
        return movieRepository.findByYear(year, pageable);
    }

    public Page<Movie> getMoviesByActor(String actorName, Pageable pageable) {
        return movieRepository.findByActors_Name(actorName, pageable);
    }

    public Page<Movie> getMoviesByCategory(String categoryName, Pageable pageable) {
        return movieRepository.findByCategories_Name(categoryName, pageable);
    }

    public Double getAverageRating(Long movieId) {

        Double avg = movieRepository.getAverageRating(movieId);

        if (avg == null) {
            return 0.0;
        }

        return avg;
    }

    public Page<Movie> getTopRatedMovies(Pageable pageable) {
        return movieRepository.findTopRatedMovies(pageable);
    }

    public Page<Movie> searchMovies(
            String title,
            Integer year,
            String actor,
            String category,
            Pageable pageable) {

        Specification<Movie> spec = Specification.where((Specification<Movie>) null);

        if (title != null) {
            spec = spec.and(MovieSpecification.titleContains(title));
        }

        if (year != null) {
            spec = spec.and(MovieSpecification.hasYear(year));
        }

        if (actor != null) {
            spec = spec.and(MovieSpecification.hasActor(actor));
        }

        if (category != null) {
            spec = spec.and(MovieSpecification.hasCategory(category));
        }

        return movieRepository.findAll(spec, pageable);
    }

    public Movie uploadMedia(Long movieId, MultipartFile image, MultipartFile video) throws Exception {

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

        return movieRepository.save(movie);
    }

}
