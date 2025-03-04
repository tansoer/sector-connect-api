package com.sectorconnect.services;

import com.sectorconnect.dto.SectorDto;
import com.sectorconnect.mappers.SectorMapper;
import com.sectorconnect.repositories.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorService {

    private final SectorRepository sectorRepository;
    private final SectorMapper sectorMapper;

    public List<SectorDto> getSectors() {
        return sectorRepository.findByParentSectorIsNull()
                .stream()
                .map(sectorMapper::toDto)
                .toList();
    }
}
