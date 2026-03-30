package com.miniprojet.wsrest.dto;

import com.miniprojet.wsrest.model.Actor;

public class ActorMapper {

    public static ActorResponseDTO toResponse(Actor actor) {
        ActorResponseDTO dto = new ActorResponseDTO();
        dto.setId(actor.getId());
        dto.setName(actor.getName());
        return dto;
    }

    public static Actor toEntity(ActorRequestDTO dto) {
        Actor actor = new Actor();
        actor.setName(dto.getName());
        return actor;
    }
}
