package com.miniprojet.wsrest.controller;

import com.miniprojet.wsrest.model.Movie;
import com.miniprojet.wsrest.service.MovieService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Movie> moviesPage = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(moviesPage.getContent());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {

        return movieService.getMovieById(id)
                .map(movie -> ResponseEntity.ok(movie))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {

        Movie createdMovie = movieService.createMovie(movie);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(
            @PathVariable Long id,
            @RequestBody Movie movie) {

        try {
            Movie updatedMovie = movieService.updateMovie(id, movie);
            return ResponseEntity.ok(updatedMovie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {

        movieService.deleteMovie(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Movie>> getMoviesByYear(
            @PathVariable int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> movies = movieService.getMoviesByYear(year, pageable);

        return ResponseEntity.ok(movies.getContent());
    }

    @GetMapping("/actor/{actorName}")
    public ResponseEntity<List<Movie>> getMoviesByActor(
            @PathVariable String actorName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> movies = movieService.getMoviesByActor(actorName, pageable);

        return ResponseEntity.ok(movies.getContent());
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<Movie>> getMoviesByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> movies = movieService.getMoviesByCategory(categoryName, pageable);

        return ResponseEntity.ok(movies.getContent());

    }

    @GetMapping("/{id}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long id) {

        Double average = movieService.getAverageRating(id);

        return ResponseEntity.ok(average);
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<Movie>> getTopRatedMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Movie> movies = movieService.getTopRatedMovies(pageable);

        return ResponseEntity.ok(movies.getContent());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Movie>> searchMovies(

            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String actor,
            @RequestParam(required = false) String category,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort

    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort));

        Page<Movie> movies = movieService.searchMovies(
                title,
                year,
                actor,
                category,
                pageable);

        return ResponseEntity.ok(movies);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/upload-media")
    public ResponseEntity<Movie> uploadMedia(
            @PathVariable Long id,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile video
    ) {

        try {

            Movie movie = movieService.uploadMedia(id, image, video);

            return ResponseEntity.ok(movie);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
