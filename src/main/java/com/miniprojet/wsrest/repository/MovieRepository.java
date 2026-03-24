package com.miniprojet.wsrest.repository;

import com.miniprojet.wsrest.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    Page<Movie> findByYear(int year, Pageable pageable);

    Page<Movie> findByActors_Name(String actorName, Pageable pageable);

    Page<Movie> findByCategories_Name(String categoryName , Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.movie.id = :movieId")
    Double getAverageRating(@Param("movieId") Long movieId);

    @Query("""
        SELECT m FROM Movie m
        JOIN m.reviews r
        GROUP BY m
        ORDER BY AVG(r.rating) DESC
    """)
    Page<Movie> findTopRatedMovies(Pageable pageable);

}