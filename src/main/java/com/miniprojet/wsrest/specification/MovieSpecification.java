package com.miniprojet.wsrest.specification;

import com.miniprojet.wsrest.model.Movie;
import com.miniprojet.wsrest.model.Actor;
import com.miniprojet.wsrest.model.Category;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

public class MovieSpecification {

    public static Specification<Movie> titleContains(String title) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Movie> hasYear(Integer year) {
        return (root, query, cb) ->
                cb.equal(root.get("year"), year);
    }

    public static Specification<Movie> hasActor(String actorName) {
        return (root, query, cb) -> {
            Join<Movie, Actor> actors = root.join("actors");
            return cb.like(cb.lower(actors.get("name")), "%" + actorName.toLowerCase() + "%");
        };
    }

    public static Specification<Movie> hasCategory(String categoryName) {
        return (root, query, cb) -> {
            Join<Movie, Category> categories = root.join("categories");
            return cb.like(cb.lower(categories.get("name")), "%" + categoryName.toLowerCase() + "%");
        };
    }

}
