package com.miniprojet.wsrest.controller;

import com.miniprojet.wsrest.model.Actor;
import com.miniprojet.wsrest.service.ActorService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "title") String sort
    ) {

        Pageable pageable = PageRequest.of(page,size);
        
        Page<Actor> actors = actorService.getAllActors(pageable);
        return ResponseEntity.ok(actors.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Actor> getActorById(@PathVariable Long id) {

        return actorService.getActorById(id)
                .map(actor -> ResponseEntity.ok(actor))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Actor> createActor(@RequestBody Actor actor) {

        Actor createdActor = actorService.createActor(actor);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdActor);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Actor> updateActor(
            @PathVariable Long id,
            @RequestBody Actor actor) {

        try {
            Actor updatedActor = actorService.updateActor(id, actor);
            return ResponseEntity.ok(updatedActor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {

        actorService.deleteActor(id);

        return ResponseEntity.noContent().build();
    }

}
