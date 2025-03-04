package com.sectorconnect;

import com.sectorconnect.dto.SectorDto;
import com.sectorconnect.entities.Sector;
import com.sectorconnect.mappers.SectorMapper;
import com.sectorconnect.repositories.SectorRepository;
import com.sectorconnect.services.SectorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SectorServiceTest {

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private SectorMapper sectorMapper;

    @InjectMocks
    private SectorService sectorService;

    @Test
    void testGetTopLevelSectors() {
        // Given
        Sector manufacturing = new Sector(1L, "Manufacturing", false, null, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        Sector service = new Sector(2L, "Service", false, null, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        Sector other = new Sector(3L, "Other", false, null, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        List<Sector> topLevelSectors = List.of(manufacturing, service, other);

        when(sectorRepository.findByParentSectorIsNull()).thenReturn(topLevelSectors);
        when(sectorMapper.toDto(any())).thenAnswer(invocation -> {
            Sector s = invocation.getArgument(0);
            return new SectorDto(s.getId(), s.getName(), s.isSelectable(), null, new ArrayList<>());
        });

        // When
        List<SectorDto> sectors = sectorService.getSectors();

        // Then
        Assertions.assertNotNull(sectors);
        Assertions.assertEquals(3, sectors.size());
        Assertions.assertEquals("Manufacturing", sectors.get(0).getName());
        Assertions.assertEquals("Service", sectors.get(1).getName());
        Assertions.assertEquals("Other", sectors.get(2).getName());
    }

    @Test
    void testGetSectorsWithSubSectors() {
        // Given
        Sector manufacturing = new Sector(1L, "Manufacturing", false, null, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        Sector machinery = new Sector(12L, "Machinery", false, manufacturing, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        Sector maritime = new Sector(97L, "Maritime", false, machinery, new HashSet<>(), new HashSet<>(), LocalDateTime.now());
        Sector boatYachtBuilding = new Sector(269L, "Boat/Yacht building", true, maritime, new HashSet<>(), new HashSet<>(), LocalDateTime.now());

        // Set up the hierarchy
        manufacturing.getSubSectors().add(machinery);
        machinery.getSubSectors().add(maritime);
        maritime.getSubSectors().add(boatYachtBuilding);

        List<Sector> topLevelSectors = List.of(manufacturing);

        when(sectorRepository.findByParentSectorIsNull()).thenReturn(topLevelSectors);

        when(sectorMapper.toDto(any())).thenAnswer(invocation -> {
            Sector s = invocation.getArgument(0);
            List<SectorDto> subSectorDtos = s.getSubSectors() != null
                    ? s.getSubSectors().stream().map(sectorMapper::toDto).toList()
                    : new ArrayList<>();

            return new SectorDto(
                    s.getId(),
                    s.getName(),
                    s.isSelectable(),
                    s.getParentSector() != null ? s.getParentSector().getId() : null,
                    subSectorDtos
            );
        });

        // When
        List<SectorDto> sectors = sectorService.getSectors();

        // Then
        Assertions.assertNotNull(sectors);
        Assertions.assertEquals(1, sectors.size());
        Assertions.assertEquals("Manufacturing", sectors.get(0).getName());

        // Verify first-level sub-sector
        List<SectorDto> subSectors1 = sectors.get(0).getSubSectors();
        Assertions.assertEquals(1, subSectors1.size());
        Assertions.assertEquals("Machinery", subSectors1.get(0).getName());

        // Verify second-level sub-sector
        List<SectorDto> subSectors2 = subSectors1.get(0).getSubSectors();
        Assertions.assertEquals(1, subSectors2.size());
        Assertions.assertEquals("Maritime", subSectors2.get(0).getName());

        // Verify third-level sub-sector
        List<SectorDto> subSectors3 = subSectors2.get(0).getSubSectors();
        Assertions.assertEquals(1, subSectors3.size());
        Assertions.assertEquals("Boat/Yacht building", subSectors3.get(0).getName());
    }

    @Test
    void testGetSectors_EmptyDatabase() {
        // Given
        when(sectorRepository.findByParentSectorIsNull()).thenReturn(List.of());

        // When
        List<SectorDto> result = sectorService.getSectors();

        // Then
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}
