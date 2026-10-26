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
import tn.esprit.rh.achat.entities.CategorieProduit;
import tn.esprit.rh.achat.services.ICategorieProduitService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategorieProduitControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICategorieProduitService categorieProduitService;

    @InjectMocks
    private CategorieProduitController categorieProduitController;

    private ObjectMapper objectMapper;
    private CategorieProduit sampleCategorie;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categorieProduitController).build();
        objectMapper = new ObjectMapper();
        sampleCategorie = new CategorieProduit();
        sampleCategorie.setIdCategorieProduit(1L);
        sampleCategorie.setCodeCategorie("C01");
        sampleCategorie.setLibelleCategorie("Electronique");
    }

    @Test
    @DisplayName("GET /categorieProduit/retrieve-all-categorieProduit should return categories")
    void testGetCategorieProduit() throws Exception {
        List<CategorieProduit> list = Arrays.asList(sampleCategorie);
        when(categorieProduitService.retrieveAllCategorieProduits()).thenReturn(list);

        mockMvc.perform(get("/categorieProduit/retrieve-all-categorieProduit"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCategorieProduit").value(1))
                .andExpect(jsonPath("$[0].libelleCategorie").value("Electronique"));

        verify(categorieProduitService, times(1)).retrieveAllCategorieProduits();
    }

    @Test
    @DisplayName("GET /categorieProduit/retrieve-categorieProduit/{categorieProduit-id} should return single category")
    void testRetrieveCategorieProduit() throws Exception {
        when(categorieProduitService.retrieveCategorieProduit(1L)).thenReturn(sampleCategorie);

        mockMvc.perform(get("/categorieProduit/retrieve-categorieProduit/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1))
                .andExpect(jsonPath("$.codeCategorie").value("C01"));

        verify(categorieProduitService, times(1)).retrieveCategorieProduit(1L);
    }

    @Test
    @DisplayName("POST /categorieProduit/add-categorieProduit should add and return category")
    void testAddCategorieProduit() throws Exception {
        when(categorieProduitService.addCategorieProduit(any(CategorieProduit.class))).thenReturn(sampleCategorie);

        mockMvc.perform(post("/categorieProduit/add-categorieProduit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCategorie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1));

        verify(categorieProduitService, times(1)).addCategorieProduit(any(CategorieProduit.class));
    }

    @Test
    @DisplayName("DELETE /categorieProduit/remove-categorieProduit/{categorieProduit-id} should delete category")
    void testRemoveCategorieProduit() throws Exception {
        doNothing().when(categorieProduitService).deleteCategorieProduit(1L);

        mockMvc.perform(delete("/categorieProduit/remove-categorieProduit/1"))
                .andExpect(status().isOk());

        verify(categorieProduitService, times(1)).deleteCategorieProduit(1L);
    }

    @Test
    @DisplayName("PUT /categorieProduit/modify-categorieProduit should update and return category")
    void testModifyCategorieProduit() throws Exception {
        when(categorieProduitService.updateCategorieProduit(any(CategorieProduit.class))).thenReturn(sampleCategorie);

        mockMvc.perform(put("/categorieProduit/modify-categorieProduit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleCategorie)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategorieProduit").value(1));

        verify(categorieProduitService, times(1)).updateCategorieProduit(any(CategorieProduit.class));
    }
}
