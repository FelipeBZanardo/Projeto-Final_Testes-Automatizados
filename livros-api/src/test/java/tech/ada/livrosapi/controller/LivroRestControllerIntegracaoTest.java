package tech.ada.livrosapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.livrosapi.LivrosApiApplication;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.model.dto.LivroResponse;


import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest(classes = LivrosApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LivroRestControllerIntegracaoTest {

    @Autowired
    private MockMvc mockMvc;
    private MvcResult mvcLivroResult;
    private Livro livro;

    private ObjectMapper mapper = new ObjectMapper();
    private LivroRequest request;

    @BeforeEach
    void BeforeEach() throws Exception {
        request = new LivroRequest("titulo", "resumo", "sumario", new BigDecimal(200), 200, "12345", LocalDate.of(2023
                , 7
                , 22));

        mvcLivroResult = mockMvc.perform(MockMvcRequestBuilders.post("/rest/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        livro = mapper.readValue(mvcLivroResult.getResponse().getContentAsString(), Livro.class);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deveAdicionarUmLivroIntegracaoTest() throws Exception {
        assertEquals(request.isbn(), livro.getIsbn());

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0]").exists());

        LivroRequest livroPassado = new LivroRequest("tituloPassado", "resumoPassado", "sumarioPassado",
                BigDecimal.valueOf(100L), 300, "555555", LocalDate.of(2022,7,22));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/rest/livros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(livroPassado)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deveRetornarUmaListaComTodosOsLivrosIntegracaoTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0]").exists())
                .andExpect(jsonPath("$.[0].isbn").value("12345"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deveRetornarUmLivroPeloIdIntegracaoTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros/{id}", livro.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Livro livroResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), Livro.class);
        assertEquals(livro.getId(), livroResponse.getId());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deveAtualizarUmLivroPeloIdIntegracaoTest() throws Exception {
        LivroRequest livroRequest = new LivroRequest("titulo2", "resumo2", "sumario2",
                BigDecimal.valueOf(200L), 500, "54321", LocalDate.of(2024, 8, 22));
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/rest/livros/{id}", livro.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(livroRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Livro livroResponse = mapper.readValue(mvcResult.getResponse().getContentAsString(), Livro.class);

        assertEquals(livroRequest.isbn(), livroResponse.getIsbn());

        MvcResult mvcLivroAlterado = mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros/{id}", livro.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(livroRequest)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Livro livroAlterado = mapper.readValue(mvcLivroAlterado.getResponse().getContentAsString(), Livro.class);
        assertEquals(livroResponse.getId(), livroAlterado.getId());

    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void deveRemoverUmLivroPeloIdIntegracaoTest() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/rest/livros/{id}", livro.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String msg = mvcResult.getResponse().getContentAsString();
        assertEquals("Livro cujo Id - %s - foi deletado com sucesso!".formatted(livro.getId()), msg);
    }

}
