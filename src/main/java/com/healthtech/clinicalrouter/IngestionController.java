package com.healthtech.clinicalrouter;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinical")
public class IngestionController {

    private final ClinicalService clinicalService;

    public IngestionController(ClinicalService clinicalService) {
        this.clinicalService = clinicalService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingestRecord(@Valid @RequestBody ClinicalRecordDto record) {
        String result = clinicalService.routeRecord(record);
        return ResponseEntity.ok(result);
    }
}