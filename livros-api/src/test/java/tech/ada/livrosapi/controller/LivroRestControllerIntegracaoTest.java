package tech.ada.livrosapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tech.ada.livrosapi.LivrosApiApplication;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.model.dto.LivroResponse;
import org.springframework.http.ResponseEntity;
import tech.ada.livrosapi.service.LivroService;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = LivrosApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LivroRestControllerIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private LivroRestController restController;
    @Mock
    private LivroService service;
    private Livro livro;
    private LivroResponse livroResponse;
    private LivroRequest livroRequest;
    private UUID newID;
    private static final BigDecimal PRECO = BigDecimal.valueOf(20.00);
    private static final UUID ID = UUID.fromString("5fa3cf7a-2836-40dc-957b-43bdac98104a");
    @BeforeEach
    void setup(){
        startLivro();
    }
    @Test
    @Order(1)
    @DisplayName("Deve adicionar livro utilizando o MockMvc - Teste de Integração")
    void deveAdicionarLivroMockMvc() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rest/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gerarJson(livroRequest)))
                //.andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        System.out.println(mvcResult.getResponse().getContentAsString().substring(7,43));
        newID = UUID.fromString(mvcResult.getResponse().getContentAsString().substring(7,43));
    }

    @Test
    @DisplayName("Deve buscar todos os livros")
    @Order(2)
    void deveBuscarTodosOsLivrosMockMvc() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].isbn").value("1234567891011"));
    }

    @Test
    @DisplayName("Deve buscar livro pelo ID")
    @Order(3)
    void deveBuscarLivroPeloIdMockMvc() throws Exception {
        doReturn(livro).when(service).create(any(LivroRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros/{id}", ID))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().json(gerarJson(livro)))
                .andExpect(status().isCreated());

    }

    private void startLivro(){
        String titulo = "Titulo";
        String resumo = "Resumo";
        String sumario = "Sumario";
        Integer numeroPaginas = 100;
        String isbn = "1234567891011";
        LocalDate dataPublicacao = LocalDate.of(2023,6,25);
        livroRequest = new LivroRequest(titulo, resumo, sumario, PRECO, numeroPaginas, isbn, dataPublicacao);
        livroResponse = new LivroResponse(ID, titulo, resumo, sumario, PRECO, numeroPaginas, isbn, dataPublicacao);
        livro = new Livro(ID, titulo, resumo, sumario, PRECO, numeroPaginas, isbn, dataPublicacao);
    }

    private String gerarJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

}
