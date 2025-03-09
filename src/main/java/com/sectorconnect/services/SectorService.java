package com.sectorconnect.services;

import com.sectorconnect.dto.SectorDto;
import com.sectorconnect.entities.Sector;
import com.sectorconnect.mappers.SectorMapper;
import com.sectorconnect.repositories.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    public List<SectorDto> getSectors() {
        return sortSectorTree(sectorRepository.findByParentSectorIsNull())
                .stream().map(sectorMapper::toDto).toList();
    }

    private List<Sector> sortSectorTree(List<Sector> sectors) {
        return sectors.stream()
                .sorted((a, b) -> Boolean.compare(a.getSubSectors().isEmpty(), b.getSubSectors().isEmpty()))
                .map(sector -> sector.toBuilder()
                        .subSectors(new LinkedHashSet<>(sortSectorTree(sector.getSubSectors().stream().toList())))
                        .build())
                .toList();
    }
}
