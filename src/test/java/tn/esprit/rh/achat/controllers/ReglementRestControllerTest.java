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
import tn.esprit.rh.achat.entities.Reglement;
import tn.esprit.rh.achat.services.IReglementService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReglementRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IReglementService reglementService;

    @InjectMocks
    private ReglementRestController reglementRestController;

    private ObjectMapper objectMapper;
    private Reglement sampleReglement;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reglementRestController).build();
        objectMapper = new ObjectMapper();
        sampleReglement = new Reglement();
        sampleReglement.setIdReglement(1L);
        sampleReglement.setMontantPaye(300.0f);
        sampleReglement.setMontantRestant(0.0f);
        sampleReglement.setPayee(true);
    }

    @Test
    @DisplayName("GET /reglement/retrieve-all-reglements should return all payments")
    void testGetReglement() throws Exception {
        List<Reglement> list = Arrays.asList(sampleReglement);
        when(reglementService.retrieveAllReglements()).thenReturn(list);

        mockMvc.perform(get("/reglement/retrieve-all-reglements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReglement").value(1))
                .andExpect(jsonPath("$[0].montantPaye").value(300.0));

        verify(reglementService, times(1)).retrieveAllReglements();
    }

    @Test
    @DisplayName("GET /reglement/retrieve-reglement/{reglement-id} should return single payment")
    void testRetrieveReglement() throws Exception {
        when(reglementService.retrieveReglement(1L)).thenReturn(sampleReglement);

        mockMvc.perform(get("/reglement/retrieve-reglement/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReglement").value(1))
                .andExpect(jsonPath("$.montantPaye").value(300.0));

        verify(reglementService, times(1)).retrieveReglement(1L);
    }

    @Test
    @DisplayName("POST /reglement/add-reglement should persist and return payment")
    void testAddReglement() throws Exception {
        when(reglementService.addReglement(any(Reglement.class))).thenReturn(sampleReglement);

        mockMvc.perform(post("/reglement/add-reglement")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleReglement)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReglement").value(1));

        verify(reglementService, times(1)).addReglement(any(Reglement.class));
    }

    @Test
    @DisplayName("GET /reglement/retrieveReglementByFacture/{facture-id} should return invoice payments")
    void testRetrieveReglementByFacture() throws Exception {
        when(reglementService.retrieveReglementByFacture(5L)).thenReturn(Arrays.asList(sampleReglement));

        mockMvc.perform(get("/reglement/retrieveReglementByFacture/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReglement").value(1));

        verify(reglementService, times(1)).retrieveReglementByFacture(5L);
    }

    @Test
    @DisplayName("GET /reglement/getChiffreAffaireEntreDeuxDate/{startDate}/{endDate} should return turnover on success")
    void testGetChiffreAffaireEntreDeuxDate_Success() throws Exception {
        when(reglementService.getChiffreAffaireEntreDeuxDate(any(Date.class), any(Date.class))).thenReturn(5000.0f);

        mockMvc.perform(get("/reglement/getChiffreAffaireEntreDeuxDate/2026-01-01/2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("5000.0"));

        verify(reglementService, times(1)).getChiffreAffaireEntreDeuxDate(any(Date.class), any(Date.class));
    }

    @Test
    @DisplayName("GET /reglement/getChiffreAffaireEntreDeuxDate/{startDate}/{endDate} returns 0 when service throws exception")
    void testGetChiffreAffaireEntreDeuxDate_ExceptionReturnsZero() throws Exception {
        when(reglementService.getChiffreAffaireEntreDeuxDate(any(Date.class), any(Date.class)))
                .thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(get("/reglement/getChiffreAffaireEntreDeuxDate/2026-01-01/2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));

        verify(reglementService, times(1)).getChiffreAffaireEntreDeuxDate(any(Date.class), any(Date.class));
    }
}
