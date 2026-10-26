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
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.services.IStockService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class StockRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IStockService stockService;

    @InjectMocks
    private StockRestController stockRestController;

    private ObjectMapper objectMapper;
    private Stock sampleStock;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(stockRestController).build();
        objectMapper = new ObjectMapper();
        sampleStock = new Stock(1L, "Stock Principal", 50, 10, null);
    }

    @Test
    @DisplayName("GET /stock/retrieve-all-stocks should return list of stocks")
    void testGetStocks() throws Exception {
        List<Stock> stocks = Arrays.asList(sampleStock);
        when(stockService.retrieveAllStocks()).thenReturn(stocks);

        mockMvc.perform(get("/stock/retrieve-all-stocks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idStock").value(1))
                .andExpect(jsonPath("$[0].libelleStock").value("Stock Principal"));

        verify(stockService, times(1)).retrieveAllStocks();
    }

    @Test
    @DisplayName("GET /stock/retrieve-stock/{stock-id} should return single stock")
    void testRetrieveStock() throws Exception {
        when(stockService.retrieveStock(1L)).thenReturn(sampleStock);

        mockMvc.perform(get("/stock/retrieve-stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStock").value(1))
                .andExpect(jsonPath("$.libelleStock").value("Stock Principal"))
                .andExpect(jsonPath("$.qte").value(50));

        verify(stockService, times(1)).retrieveStock(1L);
    }

    @Test
    @DisplayName("POST /stock/add-stock should add and return created stock")
    void testAddStock() throws Exception {
        when(stockService.addStock(any(Stock.class))).thenReturn(sampleStock);

        mockMvc.perform(post("/stock/add-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStock").value(1))
                .andExpect(jsonPath("$.libelleStock").value("Stock Principal"));

        verify(stockService, times(1)).addStock(any(Stock.class));
    }

    @Test
    @DisplayName("DELETE /stock/remove-stock/{stock-id} should delete stock")
    void testRemoveStock() throws Exception {
        doNothing().when(stockService).deleteStock(1L);

        mockMvc.perform(delete("/stock/remove-stock/1"))
                .andExpect(status().isOk());

        verify(stockService, times(1)).deleteStock(1L);
    }

    @Test
    @DisplayName("PUT /stock/modify-stock should update and return modified stock")
    void testModifyStock() throws Exception {
        when(stockService.updateStock(any(Stock.class))).thenReturn(sampleStock);

        mockMvc.perform(put("/stock/modify-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleStock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStock").value(1));

        verify(stockService, times(1)).updateStock(any(Stock.class));
    }
}
