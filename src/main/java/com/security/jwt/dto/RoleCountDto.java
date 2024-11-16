package com.security.jwt.dto;

import com.security.jwt.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleCountDto {
    private Role role;
    private long count;
}
