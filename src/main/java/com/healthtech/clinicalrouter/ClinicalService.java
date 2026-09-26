package com.healthtech.clinicalrouter;

import org.springframework.stereotype.Service;

@Service
public class ClinicalService {
    public String routeRecord(ClinicalRecordDto record) {
        if (record.getPriority() != null && record.getPriority() > 3) {
            return "Routed to High-Priority Urgent Care Stream for Patient: " + record.getPatientId();
        }
        return "Routed to Standard Clinical Queue for Patient: " + record.getPatientId();
    }
}