package tn.esprit.rh.achat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.rh.achat.entities.Operateur;
import tn.esprit.rh.achat.services.IOperateurService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OperateurControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IOperateurService operateurService;

    @InjectMocks
    private OperateurController operateurController;

    private ObjectMapper objectMapper;
    private Operateur sampleOperateur;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(operateurController).build();
        objectMapper = new ObjectMapper();
        sampleOperateur = new Operateur();
        sampleOperateur.setIdOperateur(1L);
        sampleOperateur.setNom("Ben Salah");
        sampleOperateur.setPrenom("Sami");
        sampleOperateur.setPassword("pwd123");
    }

    @Test
    @DisplayName("GET /operateur/retrieve-all-operateurs should return list of operators")
    void testGetOperateurs() throws Exception {
        List<Operateur> list = Arrays.asList(sampleOperateur);
        when(operateurService.retrieveAllOperateurs()).thenReturn(list);

        mockMvc.perform(get("/operateur/retrieve-all-operateurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOperateur").value(1))
                .andExpect(jsonPath("$[0].nom").value("Ben Salah"));

        verify(operateurService, times(1)).retrieveAllOperateurs();
    }

    @Test
    @DisplayName("GET /operateur/retrieve-operateur/{operateur-id} should return single operator")
    void testRetrieveOperateur() throws Exception {
        when(operateurService.retrieveOperateur(1L)).thenReturn(sampleOperateur);

        mockMvc.perform(get("/operateur/retrieve-operateur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOperateur").value(1))
                .andExpect(jsonPath("$.nom").value("Ben Salah"))
                .andExpect(jsonPath("$.prenom").value("Sami"));

        verify(operateurService, times(1)).retrieveOperateur(1L);
    }

    @Test
    @DisplayName("POST /operateur/add-operateur should add and return operator")
    void testAddOperateur() throws Exception {
        when(operateurService.addOperateur(any(Operateur.class))).thenReturn(sampleOperateur);

        mockMvc.perform(post("/operateur/add-operateur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleOperateur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOperateur").value(1));

        verify(operateurService, times(1)).addOperateur(any(Operateur.class));
    }

    @Test
    @DisplayName("DELETE /operateur/remove-operateur/{operateur-id} should delete operator")
    void testRemoveOperateur() throws Exception {
        doNothing().when(operateurService).deleteOperateur(1L);

        mockMvc.perform(delete("/operateur/remove-operateur/1"))
                .andExpect(status().isOk());

        verify(operateurService, times(1)).deleteOperateur(1L);
    }

    @Test
    @DisplayName("PUT /operateur/modify-operateur should update and return operator")
    void testModifyOperateur() throws Exception {
        when(operateurService.updateOperateur(any(Operateur.class))).thenReturn(sampleOperateur);

        mockMvc.perform(put("/operateur/modify-operateur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleOperateur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOperateur").value(1));

        verify(operateurService, times(1)).updateOperateur(any(Operateur.class));
    }
}
