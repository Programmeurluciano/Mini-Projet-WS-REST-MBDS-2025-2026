package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.model.Actor;
import com.miniprojet.wsrest.repository.ActorRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Page<Actor> getAllActors(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    public Optional<Actor> getActorById(Long id) {
        return actorRepository.findById(id);
    }

    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

    public void deleteActor(Long id) {
        actorRepository.deleteById(id);
    }

    public Actor updateActor(Long id, Actor updatedActor) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actor.setName(updatedActor.getName());
                    return actorRepository.save(actor);
                })
                .orElseThrow(() -> new RuntimeException("Actor not found with id " + id));
    }

}
