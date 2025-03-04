package com.sectorconnect.mappers;

import com.sectorconnect.dto.SectorDto;
import com.sectorconnect.entities.Sector;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SectorMapper {

    public SectorDto toDto(Sector sector) {
        if (sector == null) return null;

        SectorDto dto = new SectorDto();
        dto.setId(sector.getId());
        dto.setName(sector.getName());
        dto.setSelectable(sector.isSelectable());
        dto.setParentId(sector.getParentSector() != null ? sector.getParentSector().getId() : null);

        List<SectorDto> subSectorsDtos = sector.getSubSectors().stream()
                .map(this::toDto)
                .toList();
        dto.setSubSectors(subSectorsDtos);

        return dto;
    }
}
