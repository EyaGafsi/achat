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
import tn.esprit.rh.achat.entities.Facture;
import tn.esprit.rh.achat.services.IFactureService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FactureRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IFactureService factureService;

    @InjectMocks
    private FactureRestController factureRestController;

    private ObjectMapper objectMapper;
    private Facture sampleFacture;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(factureRestController).build();
        objectMapper = new ObjectMapper();
        sampleFacture = new Facture();
        sampleFacture.setIdFacture(1L);
        sampleFacture.setMontantFacture(1500.0f);
        sampleFacture.setMontantRemise(150.0f);
        sampleFacture.setArchivee(false);
    }

    @Test
    @DisplayName("GET /facture/retrieve-all-factures should return list of invoices")
    void testGetFactures() throws Exception {
        List<Facture> list = Arrays.asList(sampleFacture);
        when(factureService.retrieveAllFactures()).thenReturn(list);

        mockMvc.perform(get("/facture/retrieve-all-factures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFacture").value(1))
                .andExpect(jsonPath("$[0].montantFacture").value(1500.0));

        verify(factureService, times(1)).retrieveAllFactures();
    }

    @Test
    @DisplayName("GET /facture/retrieve-facture/{facture-id} should return single invoice")
    void testRetrieveFacture() throws Exception {
        when(factureService.retrieveFacture(1L)).thenReturn(sampleFacture);

        mockMvc.perform(get("/facture/retrieve-facture/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFacture").value(1))
                .andExpect(jsonPath("$.montantFacture").value(1500.0));

        verify(factureService, times(1)).retrieveFacture(1L);
    }

    @Test
    @DisplayName("POST /facture/add-facture should persist and return invoice")
    void testAddFacture() throws Exception {
        when(factureService.addFacture(any(Facture.class))).thenReturn(sampleFacture);

        mockMvc.perform(post("/facture/add-facture")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleFacture)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFacture").value(1));

        verify(factureService, times(1)).addFacture(any(Facture.class));
    }

    @Test
    @DisplayName("PUT /facture/cancel-facture/{facture-id} should cancel invoice")
    void testCancelFacture() throws Exception {
        doNothing().when(factureService).cancelFacture(1L);

        mockMvc.perform(put("/facture/cancel-facture/1"))
                .andExpect(status().isOk());

        verify(factureService, times(1)).cancelFacture(1L);
    }

    @Test
    @DisplayName("GET /facture/getFactureByFournisseur/{fournisseur-id} should return supplier invoices")
    void testGetFactureByFournisseur() throws Exception {
        when(factureService.getFacturesByFournisseur(10L)).thenReturn(Arrays.asList(sampleFacture));

        mockMvc.perform(get("/facture/getFactureByFournisseur/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFacture").value(1));

        verify(factureService, times(1)).getFacturesByFournisseur(10L);
    }

    @Test
    @DisplayName("PUT /facture/assignOperateurToFacture/{idOperateur}/{idFacture} should assign operator")
    void testAssignOperateurToFacture() throws Exception {
        doNothing().when(factureService).assignOperateurToFacture(2L, 1L);

        mockMvc.perform(put("/facture/assignOperateurToFacture/2/1"))
                .andExpect(status().isOk());

        verify(factureService, times(1)).assignOperateurToFacture(2L, 1L);
    }

    @Test
    @DisplayName("GET /facture/pourcentageRecouvrement/{startDate}/{endDate} returns recovery rate on success")
    void testPourcentageRecouvrement_Success() throws Exception {
        when(factureService.pourcentageRecouvrement(any(Date.class), any(Date.class))).thenReturn(75.5f);

        mockMvc.perform(get("/facture/pourcentageRecouvrement/2026-01-01/2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("75.5"));

        verify(factureService, times(1)).pourcentageRecouvrement(any(Date.class), any(Date.class));
    }

    @Test
    @DisplayName("GET /facture/pourcentageRecouvrement/{startDate}/{endDate} returns 0 when service throws exception")
    void testPourcentageRecouvrement_ExceptionReturnsZero() throws Exception {
        when(factureService.pourcentageRecouvrement(any(Date.class), any(Date.class)))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/facture/pourcentageRecouvrement/2026-01-01/2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("0.0"));

        verify(factureService, times(1)).pourcentageRecouvrement(any(Date.class), any(Date.class));
    }
}
