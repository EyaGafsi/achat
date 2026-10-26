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
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.services.IProduitService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProduitRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IProduitService produitService;

    @InjectMocks
    private ProduitRestController produitRestController;

    private ObjectMapper objectMapper;
    private Produit sampleProduit;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produitRestController).build();
        objectMapper = new ObjectMapper();
        sampleProduit = new Produit();
        sampleProduit.setIdProduit(1L);
        sampleProduit.setCodeProduit("P100");
        sampleProduit.setLibelleProduit("Smartphone");
        sampleProduit.setPrix(1200.0f);
    }

    @Test
    @DisplayName("GET /produit/retrieve-all-produits should return list of products")
    void testGetProduits() throws Exception {
        List<Produit> list = Arrays.asList(sampleProduit);
        when(produitService.retrieveAllProduits()).thenReturn(list);

        mockMvc.perform(get("/produit/retrieve-all-produits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProduit").value(1))
                .andExpect(jsonPath("$[0].libelleProduit").value("Smartphone"));

        verify(produitService, times(1)).retrieveAllProduits();
    }

    @Test
    @DisplayName("GET /produit/retrieve-produit/{produit-id} should return single product")
    void testRetrieveRayon() throws Exception {
        when(produitService.retrieveProduit(1L)).thenReturn(sampleProduit);

        mockMvc.perform(get("/produit/retrieve-produit/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduit").value(1))
                .andExpect(jsonPath("$.codeProduit").value("P100"));

        verify(produitService, times(1)).retrieveProduit(1L);
    }

    @Test
    @DisplayName("POST /produit/add-produit should persist and return product")
    void testAddProduit() throws Exception {
        when(produitService.addProduit(any(Produit.class))).thenReturn(sampleProduit);

        mockMvc.perform(post("/produit/add-produit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleProduit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduit").value(1))
                .andExpect(jsonPath("$.libelleProduit").value("Smartphone"));

        verify(produitService, times(1)).addProduit(any(Produit.class));
    }

    @Test
    @DisplayName("DELETE /produit/remove-produit/{produit-id} should delete product")
    void testRemoveProduit() throws Exception {
        doNothing().when(produitService).deleteProduit(1L);

        mockMvc.perform(delete("/produit/remove-produit/1"))
                .andExpect(status().isOk());

        verify(produitService, times(1)).deleteProduit(1L);
    }

    @Test
    @DisplayName("PUT /produit/modify-produit should update and return product")
    void testModifyProduit() throws Exception {
        when(produitService.updateProduit(any(Produit.class))).thenReturn(sampleProduit);

        mockMvc.perform(put("/produit/modify-produit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleProduit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProduit").value(1));

        verify(produitService, times(1)).updateProduit(any(Produit.class));
    }

    @Test
    @DisplayName("PUT /produit/assignProduitToStock/{idProduit}/{idStock} should assign product to stock")
    void testAssignProduitToStock() throws Exception {
        doNothing().when(produitService).assignProduitToStock(1L, 2L);

        mockMvc.perform(put("/produit/assignProduitToStock/1/2"))
                .andExpect(status().isOk());

        verify(produitService, times(1)).assignProduitToStock(1L, 2L);
    }
}
