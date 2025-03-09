package com.sectorconnect.mappers;

import com.sectorconnect.dto.SectorDto;
import com.sectorconnect.entities.Sector;
import org.springframework.stereotype.Component;

@Component
public class SectorMapper {

    public SectorDto toDto(Sector sector) {
        if (sector == null) return null;

        return new SectorDto(
                sector.getId(),
                sector.getName(),
                sector.isSelectable(),
                sector.getParentSector() != null ? sector.getParentSector().getId() : null,
                sector.getSubSectors().stream().map(this::toDto).toList()
        );
    }
}
