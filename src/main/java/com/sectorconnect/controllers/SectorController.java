package com.sectorconnect.controllers;

import com.sectorconnect.dto.SectorDto;
import com.sectorconnect.services.SectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sectors")
@RequiredArgsConstructor
public class SectorController {

    private final SectorService sectorService;

    @GetMapping
    public ResponseEntity<List<SectorDto>> getSectors() {
        return ResponseEntity.ok(sectorService.getSectors());
    }
}
