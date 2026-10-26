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
import tn.esprit.rh.achat.entities.CategorieFournisseur;
import tn.esprit.rh.achat.entities.Fournisseur;
import tn.esprit.rh.achat.services.IFournisseurService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FournisseurRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IFournisseurService fournisseurService;

    @InjectMocks
    private FournisseurRestController fournisseurRestController;

    private ObjectMapper objectMapper;
    private Fournisseur sampleFournisseur;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(fournisseurRestController).build();
        objectMapper = new ObjectMapper();
        sampleFournisseur = new Fournisseur();
        sampleFournisseur.setIdFournisseur(1L);
        sampleFournisseur.setCode("F001");
        sampleFournisseur.setLibelle("Fournisseur 1");
        sampleFournisseur.setCategorieFournisseur(CategorieFournisseur.ORDINAIRE);
    }

    @Test
    @DisplayName("GET /fournisseur/retrieve-all-fournisseurs should return list of suppliers")
    void testGetFournisseurs() throws Exception {
        List<Fournisseur> list = Arrays.asList(sampleFournisseur);
        when(fournisseurService.retrieveAllFournisseurs()).thenReturn(list);

        mockMvc.perform(get("/fournisseur/retrieve-all-fournisseurs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFournisseur").value(1))
                .andExpect(jsonPath("$[0].code").value("F001"));

        verify(fournisseurService, times(1)).retrieveAllFournisseurs();
    }

    @Test
    @DisplayName("GET /fournisseur/retrieve-fournisseur/{fournisseur-id} should return single supplier")
    void testRetrieveFournisseur() throws Exception {
        when(fournisseurService.retrieveFournisseur(1L)).thenReturn(sampleFournisseur);

        mockMvc.perform(get("/fournisseur/retrieve-fournisseur/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFournisseur").value(1))
                .andExpect(jsonPath("$.libelle").value("Fournisseur 1"));

        verify(fournisseurService, times(1)).retrieveFournisseur(1L);
    }

    @Test
    @DisplayName("POST /fournisseur/add-fournisseur should add and return supplier")
    void testAddFournisseur() throws Exception {
        when(fournisseurService.addFournisseur(any(Fournisseur.class))).thenReturn(sampleFournisseur);

        mockMvc.perform(post("/fournisseur/add-fournisseur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleFournisseur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFournisseur").value(1));

        verify(fournisseurService, times(1)).addFournisseur(any(Fournisseur.class));
    }

    @Test
    @DisplayName("DELETE /fournisseur/remove-fournisseur/{fournisseur-id} should delete supplier")
    void testRemoveFournisseur() throws Exception {
        doNothing().when(fournisseurService).deleteFournisseur(1L);

        mockMvc.perform(delete("/fournisseur/remove-fournisseur/1"))
                .andExpect(status().isOk());

        verify(fournisseurService, times(1)).deleteFournisseur(1L);
    }

    @Test
    @DisplayName("PUT /fournisseur/modify-fournisseur should update supplier")
    void testModifyFournisseur() throws Exception {
        when(fournisseurService.updateFournisseur(any(Fournisseur.class))).thenReturn(sampleFournisseur);

        mockMvc.perform(put("/fournisseur/modify-fournisseur")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleFournisseur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFournisseur").value(1));

        verify(fournisseurService, times(1)).updateFournisseur(any(Fournisseur.class));
    }

    @Test
    @DisplayName("PUT /fournisseur/assignSecteurActiviteToFournisseur/{idSecteurActivite}/{idFournisseur} assigns sector")
    void testAssignSecteurActiviteToFournisseur() throws Exception {
        doNothing().when(fournisseurService).assignSecteurActiviteToFournisseur(1L, 2L);

        mockMvc.perform(put("/fournisseur/assignSecteurActiviteToFournisseur/1/2"))
                .andExpect(status().isOk());

        verify(fournisseurService, times(1)).assignSecteurActiviteToFournisseur(1L, 2L);
    }
}
