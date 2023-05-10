package tech.ada.livrosapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.model.dto.LivroResponse;
import tech.ada.livrosapi.service.LivroService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroControllerTest {
    @InjectMocks
    private LivroController livroController;
    @Mock
    private LivroService livroService;
    @Mock
    private ModelMapper modelMapper;
    private MockMvc mockMvc;
    private LivroRequest livroRequest;
    private LivroResponse livroResponse;
    private Livro livro;
    private String livroJson;
    private static final UUID ID = UUID.fromString("5fa3cf7a-2836-40dc-957b-43bdac98104a");
    private static final String TITULO = "Titulo";
    private static final String RESUMO = "Resumo";
    private static final String SUMARIO = "Sumario";
    private static final BigDecimal PRECO = BigDecimal.valueOf(20.00);
    private static final int NUMERO_PAGINAS = 100;
    private static final String ISBN = "1234567891011";
    private static final LocalDate DATA_PUBLICACAO = LocalDate.of(2023,6,25);

    @BeforeEach
    void setUp() throws JsonProcessingException {
        this.mockMvc = MockMvcBuilders.standaloneSetup(livroController).build();
        livroRequest = new LivroRequest(TITULO, RESUMO, SUMARIO, PRECO, NUMERO_PAGINAS, ISBN, DATA_PUBLICACAO);
        livroResponse = new LivroResponse(ID, TITULO, RESUMO, SUMARIO, PRECO, NUMERO_PAGINAS, ISBN, DATA_PUBLICACAO);
        livro = new Livro(ID, TITULO, RESUMO, SUMARIO, PRECO, NUMERO_PAGINAS, ISBN, DATA_PUBLICACAO);
        ObjectMapper objectMapper = new ObjectMapper();
        livroJson = objectMapper.writeValueAsString(livroResponse);
    }

    @Test
    void deveCriarLivroMockMvc() throws Exception {
        doReturn(livro).when(livroService).create(any(LivroRequest.class));
        doReturn(livroResponse).when(modelMapper).map(any(Livro.class), any());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(livroJson))
                .andReturn();

        verify(livroService).create(livroRequest);
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(livroJson, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void deveObterListaDeLivrosMockMvc() throws Exception {
       LivroResponse livroResponse1 = new LivroResponse(UUID.randomUUID(), TITULO + " 1", RESUMO  + " 1",
                SUMARIO  + " 1", PRECO.add(new BigDecimal("10.00")), NUMERO_PAGINAS + 100,
                "234567891011", DATA_PUBLICACAO.plusYears(1));
        LivroResponse livroResponse2 = new LivroResponse(UUID.randomUUID(), TITULO + " 2", RESUMO  + " 2",
                SUMARIO  + " 2", PRECO.add(new BigDecimal("20.00")), NUMERO_PAGINAS + 200,
                "3456789101112", DATA_PUBLICACAO.plusYears(2));

        Livro livro1 = new Livro(UUID.randomUUID(), TITULO + " 1", RESUMO  + " 1",
                SUMARIO  + " 1", PRECO.add(new BigDecimal("10.00")), NUMERO_PAGINAS + 100,
                "234567891011", DATA_PUBLICACAO.plusYears(1));
        Livro livro2 = new Livro(UUID.randomUUID(), TITULO + " 2", RESUMO  + " 2",
                SUMARIO  + " 2", PRECO.add(new BigDecimal("20.00")), NUMERO_PAGINAS + 200,
                "3456789101112", DATA_PUBLICACAO.plusYears(2));
        List<Livro> livros = List.of(livro, livro1, livro2);

        ObjectMapper objectMapper = new ObjectMapper();
        String listJson = objectMapper.writeValueAsString(List.of(livroResponse, livroResponse1, livroResponse2));
        doReturn(livros).when(livroService).getAll();
        doReturn(livroResponse).when(modelMapper).map(livro, LivroResponse.class);
        doReturn(livroResponse1).when(modelMapper).map(livro1, LivroResponse.class);
        doReturn(livroResponse2).when(modelMapper).map(livro2, LivroResponse.class);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/livros"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(listJson))
                .andReturn();
        verify(livroService).getAll();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(listJson, mvcResult.getResponse().getContentAsString());

    }

    @Test
    void deveObterLivroPorIdMockMvc() throws Exception {
        doReturn(livro).when(livroService).getById(any(UUID.class));
        doReturn(livroResponse).when(modelMapper).map(any(Livro.class), any());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/livros/{id}", ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(livroJson))
                .andReturn();
        verify(livroService).getById(ID);
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(livroJson, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void deveAtualizarLivroMockMvc() throws Exception {
        doReturn(livroResponse).when(modelMapper).map(any(Livro.class), any());
        doReturn(livro).when(livroService).update(any(UUID.class), any(LivroRequest.class));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/livros/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(livroJson))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().json(livroJson))
                .andReturn();
        verify(livroService).update(ID, livroRequest);
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(livroJson, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void delete() throws Exception {
        String esperado = "Livro cujo Id - %s - foi deletado com sucesso!".formatted(ID);
        doNothing().when(livroService).delete(any(UUID.class));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/livros/{id}", ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(esperado))
                .andReturn();

        verify(livroService).delete(ID);
        assertEquals(esperado, mvcResult.getResponse().getContentAsString());
        assertEquals(200, mvcResult.getResponse().getStatus());

    }

}