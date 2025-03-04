package com.sectorconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private boolean agreedToTerms;

    @NotEmpty(message = "At least one sector must be selected")
    @Builder.Default
    private Set<Long> sectorIds = new HashSet<>();
}
