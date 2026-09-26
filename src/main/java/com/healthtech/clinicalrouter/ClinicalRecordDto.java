package com.healthtech.clinicalrouter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClinicalRecordDto {
    @NotBlank(message = "Patient ID cannot be blank")
    private String patientId;

    @NotBlank(message = "Diagnosis code cannot be blank")
    private String diagnosisCode;

    @NotNull(message = "Priority level is required")
    private Integer priority;

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getDiagnosisCode() { return diagnosisCode; }
    public void setDiagnosisCode(String diagnosisCode) { this.diagnosisCode = diagnosisCode; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
}