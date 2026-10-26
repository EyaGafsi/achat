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
import tn.esprit.rh.achat.entities.SecteurActivite;
import tn.esprit.rh.achat.services.ISecteurActiviteService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SecteurActiviteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ISecteurActiviteService secteurActiviteService;

    @InjectMocks
    private SecteurActiviteController secteurActiviteController;

    private ObjectMapper objectMapper;
    private SecteurActivite sampleSecteur;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(secteurActiviteController).build();
        objectMapper = new ObjectMapper();
        sampleSecteur = new SecteurActivite();
        sampleSecteur.setIdSecteurActivite(1L);
        sampleSecteur.setCodeSecteurActivite("S01");
        sampleSecteur.setLibelleSecteurActivite("Textile");
    }

    @Test
    @DisplayName("GET /secteurActivite/retrieve-all-secteurActivite should return sectors")
    void testGetSecteurActivite() throws Exception {
        List<SecteurActivite> list = Arrays.asList(sampleSecteur);
        when(secteurActiviteService.retrieveAllSecteurActivite()).thenReturn(list);

        mockMvc.perform(get("/secteurActivite/retrieve-all-secteurActivite"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idSecteurActivite").value(1))
                .andExpect(jsonPath("$[0].libelleSecteurActivite").value("Textile"));

        verify(secteurActiviteService, times(1)).retrieveAllSecteurActivite();
    }

    @Test
    @DisplayName("GET /secteurActivite/retrieve-secteurActivite/{secteurActivite-id} should return single sector")
    void testRetrieveSecteurActivite() throws Exception {
        when(secteurActiviteService.retrieveSecteurActivite(1L)).thenReturn(sampleSecteur);

        mockMvc.perform(get("/secteurActivite/retrieve-secteurActivite/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSecteurActivite").value(1))
                .andExpect(jsonPath("$.codeSecteurActivite").value("S01"));

        verify(secteurActiviteService, times(1)).retrieveSecteurActivite(1L);
    }

    @Test
    @DisplayName("POST /secteurActivite/add-secteurActivite should add and return sector")
    void testAddSecteurActivite() throws Exception {
        when(secteurActiviteService.addSecteurActivite(any(SecteurActivite.class))).thenReturn(sampleSecteur);

        mockMvc.perform(post("/secteurActivite/add-secteurActivite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSecteur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSecteurActivite").value(1));

        verify(secteurActiviteService, times(1)).addSecteurActivite(any(SecteurActivite.class));
    }

    @Test
    @DisplayName("DELETE /secteurActivite/remove-secteurActivite/{secteurActivite-id} should delete sector")
    void testRemoveSecteurActivite() throws Exception {
        doNothing().when(secteurActiviteService).deleteSecteurActivite(1L);

        mockMvc.perform(delete("/secteurActivite/remove-secteurActivite/1"))
                .andExpect(status().isOk());

        verify(secteurActiviteService, times(1)).deleteSecteurActivite(1L);
    }

    @Test
    @DisplayName("PUT /secteurActivite/modify-secteurActivite should update sector")
    void testModifySecteurActivite() throws Exception {
        when(secteurActiviteService.updateSecteurActivite(any(SecteurActivite.class))).thenReturn(sampleSecteur);

        mockMvc.perform(put("/secteurActivite/modify-secteurActivite")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleSecteur)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSecteurActivite").value(1));

        verify(secteurActiviteService, times(1)).updateSecteurActivite(any(SecteurActivite.class));
    }
}
