package com.sectorconnect.mappers;

import com.sectorconnect.dto.RegisterUserDto;
import com.sectorconnect.dto.UpdateUserDto;
import com.sectorconnect.entities.Sector;
import com.sectorconnect.entities.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UpdateUserDto toDto(User user) {
        if (user == null) return null;

        UpdateUserDto dto = new UpdateUserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setAgreedToTerms(user.isAgreedToTerms());

        Set<Long> sectorIds = user.getSectors().stream()
                .map(Sector::getId)
                .collect(Collectors.toSet());
        dto.setSectorIds(sectorIds);

        return dto;
    }

    public User toEntity(RegisterUserDto registerUserDto) {
        if (registerUserDto == null) return null;

        return User.builder()
                .name(registerUserDto.getName())
                .username(registerUserDto.getUsername())
                .build();
    }
}
