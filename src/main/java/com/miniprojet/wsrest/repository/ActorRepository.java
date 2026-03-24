package com.miniprojet.wsrest.repository;

import com.miniprojet.wsrest.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorRepository extends JpaRepository<Actor, Long> {}
