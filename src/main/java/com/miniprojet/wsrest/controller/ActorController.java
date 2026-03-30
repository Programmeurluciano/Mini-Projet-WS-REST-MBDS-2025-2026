package com.miniprojet.wsrest.controller;

import com.miniprojet.wsrest.dto.ActorRequestDTO;
import com.miniprojet.wsrest.dto.ActorResponseDTO;
import com.miniprojet.wsrest.service.ActorService;

import jakarta.validation.Valid;

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
    public ResponseEntity<List<ActorResponseDTO>> getAllActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ActorResponseDTO> actors = actorService.getAllActors(pageable);
        return ResponseEntity.ok(actors.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> getActorById(@PathVariable Long id) {
        return actorService.getActorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ActorResponseDTO> createActor(
            @Valid @RequestBody ActorRequestDTO dto) {
        ActorResponseDTO created = actorService.createActor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ActorResponseDTO> updateActor(
            @PathVariable Long id,
            @Valid @RequestBody ActorRequestDTO dto) {
        try {
            ActorResponseDTO updated = actorService.updateActor(id, dto);
            return ResponseEntity.ok(updated);
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
