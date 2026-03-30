package com.miniprojet.wsrest.dto;

import com.miniprojet.wsrest.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponseDTO {

    private Long id;
    private String fullname;
    private String email;
    private boolean enabled;
    private Role role;
}
