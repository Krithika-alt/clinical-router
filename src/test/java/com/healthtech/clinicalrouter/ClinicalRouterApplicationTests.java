package com.healthtech.clinicalrouter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class ClinicalRouterApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testHighPriorityRouting() throws Exception {
        ClinicalRecordDto record = new ClinicalRecordDto();
        record.setPatientId("PATIENT-001");
        record.setDiagnosisCode("E11");
        record.setPriority(5);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clinical/ingest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Routed to High-Priority Urgent Care Stream for Patient: PATIENT-001"));
    }

    @Test
    void testValidationFailureWhenPriorityMissing() throws Exception {
        ClinicalRecordDto record = new ClinicalRecordDto();
        record.setPatientId("PATIENT-002");
        record.setDiagnosisCode("J45");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clinical/ingest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}