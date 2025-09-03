package com.preclinical.platform.preclinicaldataplatform.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.preclinical.platform.preclinicaldataplatform.dto.CreateStudyRequest;
import com.preclinical.platform.preclinicaldataplatform.entity.Study;
import com.preclinical.platform.preclinicaldataplatform.service.StudyManagementService;

@WebMvcTest(StudyController.class)
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudyManagementService studyManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "study_manager", roles = {"STUDY_MANAGER", "VIEWER"})
    void shouldGetStudyById() throws Exception {
        // Given
        Long studyId = 1L;
        Study study = Study.builder()
                .id(studyId)
                .studyCode("STUDY-001")
                .title("Phase II Efficacy Study")
                .phase(Study.StudyPhase.PHASE_II)
                .status(Study.StudyStatus.ACTIVE)
                .build();

        when(studyManagementService.getStudyById(studyId)).thenReturn(study);

        // When & Then
        mockMvc.perform(get("/api/studies/{id}", studyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studyCode").value("STUDY-001"))
                .andExpect(jsonPath("$.title").value("Phase II Efficacy Study"))
                .andExpect(jsonPath("$.phase").value("PHASE_II"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @WithMockUser(username = "study_manager", roles = {"STUDY_MANAGER", "VIEWER"})
    void shouldCreateStudy() throws Exception {
        // Given
        CreateStudyRequest request = CreateStudyRequest.builder()
                .studyCode("STUDY-002")
                .title("New Clinical Trial")
                .phase(Study.StudyPhase.PHASE_I)
                .startDate(LocalDate.now().plusDays(30))
                .build();

        Study createdStudy = Study.builder()
                .id(1L)
                .studyCode(request.getStudyCode())
                .title(request.getTitle())
                .phase(request.getPhase())
                .startDate(request.getStartDate())
                .status(Study.StudyStatus.PLANNED)
                .build();

        when(studyManagementService.createStudy(any(CreateStudyRequest.class))).thenReturn(createdStudy);

        // When & Then
        mockMvc.perform(post("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.studyCode").value("STUDY-002"))
                .andExpect(jsonPath("$.status").value("PLANNED"));
    }

    @Test
    @WithMockUser(username = "study_manager", roles = {"STUDY_MANAGER", "VIEWER"})
    void shouldReturnValidationErrorForInvalidStudyRequest() throws Exception {
        // Given
        CreateStudyRequest invalidRequest = CreateStudyRequest.builder()
                .studyCode("") // Invalid: empty study code
                .title("A") // Invalid: too short
                .build();

        // When & Then
        mockMvc.perform(post("/api/studies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
