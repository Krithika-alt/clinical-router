package com.healthtech.clinicalrouter;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.TreeMap;
import java.util.PriorityQueue;
import java.util.Optional;

@Service
public class TranscriptService {

    private final PriorityQueue<TranscriptDTO> triageQueue = new PriorityQueue<>((a, b) -> {
        return getUrgencyScore(b.urgencyLevel()) - getUrgencyScore(a.urgencyLevel());
    });

    private final Map<String, String> medicalCodeCache = new TreeMap<>();

    @PostConstruct
    public void loadCache() {
        medicalCodeCache.put("cardiac arrest", "ICD-10: I46.9 (Critical Cardiac Event)");
        medicalCodeCache.put("chest pain", "ICD-10: R07.9 (Chest Pain, Unspecified)");
        medicalCodeCache.put("physical", "ICD-10: Z00.00 (General Adult Exam)");
        System.out.println("\n[SYSTEM] Loaded BST In-Memory Medical Code Cache.");
    }

    public void addTranscript(TranscriptDTO transcript) {
        triageQueue.offer(transcript);
        System.out.println("[SERVICE] Added patient " + transcript.patientId() + ". Queue size: " + triageQueue.size());
    }

    public Optional<String> processNextTranscript() {
        if (triageQueue.isEmpty()) {
            System.out.println("[SERVICE] Queue is empty. No patients to process.");
            return Optional.empty();
        }

        // .poll() removes the highest priority item from the Max-Heap
        TranscriptDTO urgentPatient = triageQueue.poll();
        System.out.println("\n--- [PROCESSING PATIENT: " + urgentPatient.patientId() + "] ---");

        String text = urgentPatient.clinicalText().toLowerCase();
        String assignedCode = "PENDING MANUAL REVIEW";

        for (String keyword : medicalCodeCache.keySet()) {
            if (text.contains(keyword)) {
                assignedCode = medicalCodeCache.get(keyword);
                break;
            }
        }

        System.out.println("Assigned Code: " + assignedCode);
        return Optional.of(assignedCode);
    }

    // Helper method for the Max-Heap weights
    private static int getUrgencyScore(String level) {
        if (level == null) return 1;
        return switch (level.toUpperCase()) {
            case "CRITICAL" -> 3;
            case "URGENT" -> 2;
            default -> 1;
        };
    }
}