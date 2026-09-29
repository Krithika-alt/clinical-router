package com.healthtech.clinicalrouter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TranscriptServiceTest {

    private TranscriptService service;

    @BeforeEach
    void setUp() {
        service = new TranscriptService();
        service.loadCache();
    }

    @Test
    void processesCriticalBeforeUrgentBeforeRoutine() {
        service.addTranscript(new TranscriptDTO("P1", "ROUTINE", "annual physical"));
        service.addTranscript(new TranscriptDTO("P2", "CRITICAL", "cardiac arrest in waiting room"));
        service.addTranscript(new TranscriptDTO("P3", "URGENT", "chest pain"));

        assertEquals("ICD-10: I46.9 (Critical Cardiac Event)", service.processNextTranscript().orElseThrow());
        assertEquals("ICD-10: R07.9 (Chest Pain, Unspecified)", service.processNextTranscript().orElseThrow());
        assertEquals("ICD-10: Z00.00 (General Adult Exam)", service.processNextTranscript().orElseThrow());
    }

    @Test
    void keywordMatchingIsCaseInsensitive() {
        service.addTranscript(new TranscriptDTO("P1", "URGENT", "CHEST PAIN radiating to arm"));

        assertEquals("ICD-10: R07.9 (Chest Pain, Unspecified)", service.processNextTranscript().orElseThrow());
    }

    @Test
    void unmatchedTextIsFlaggedForManualReview() {
        service.addTranscript(new TranscriptDTO("P1", "ROUTINE", "sprained ankle"));

        assertEquals("PENDING MANUAL REVIEW", service.processNextTranscript().orElseThrow());
    }

    @Test
    void emptyQueueReturnsNothing() {
        assertTrue(service.processNextTranscript().isEmpty());
    }
}