package com.sectorconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorDto {

    private Long id;
    private String name;
    private boolean isSelectable;
    private Long parentId;
    @Builder.Default
    private List<SectorDto> subSectors = new ArrayList<>();
}
