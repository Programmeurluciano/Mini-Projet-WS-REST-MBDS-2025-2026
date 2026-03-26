package com.miniprojet.wsrest.service;

import com.miniprojet.wsrest.dto.ActorMapper;
import com.miniprojet.wsrest.dto.ActorRequestDTO;
import com.miniprojet.wsrest.dto.ActorResponseDTO;
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

    public Page<ActorResponseDTO> getAllActors(Pageable pageable) {
        return actorRepository.findAll(pageable)
                .map(ActorMapper::toResponse);
    }

    public Optional<ActorResponseDTO> getActorById(Long id) {
        return actorRepository.findById(id)
                .map(ActorMapper::toResponse);
    }

    public ActorResponseDTO createActor(ActorRequestDTO dto) {
        Actor actor = ActorMapper.toEntity(dto);
        return ActorMapper.toResponse(actorRepository.save(actor));
    }

    public ActorResponseDTO updateActor(Long id, ActorRequestDTO dto) {
        return actorRepository.findById(id)
                .map(actor -> {
                    actor.setName(dto.getName());
                    return ActorMapper.toResponse(actorRepository.save(actor));
                })
                .orElseThrow(() -> new RuntimeException("Actor not found with id " + id));
    }

    public void deleteActor(Long id) {
        actorRepository.deleteById(id);
    }
}
