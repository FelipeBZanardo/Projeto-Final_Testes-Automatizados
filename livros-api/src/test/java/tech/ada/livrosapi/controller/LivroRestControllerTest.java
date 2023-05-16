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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.model.dto.LivroResponse;
import tech.ada.livrosapi.service.LivroService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LivroRestControllerTest {
    @InjectMocks
    private LivroRestController livroRestController;
    @Mock
    private LivroService livroService;
    @Mock
    private ModelMapper modelMapper;
    private MockMvc mockMvc;
    private LivroRequest livroRequest;
    private LivroResponse livroResponse;
    private Livro livro;
    private static final UUID ID = UUID.fromString("5fa3cf7a-2836-40dc-957b-43bdac98104a");
    private static final BigDecimal PRECO = BigDecimal.valueOf(20.00);

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(livroRestController).build();
        startLivro();
    }

    @Test
    void deveCriarLivroMockMvc() throws Exception {
        doReturn(livro).when(livroService).create(any(LivroRequest.class));
        doReturn(livroResponse).when(modelMapper).map(any(Livro.class), any());
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/livros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gerarJson(livroRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(gerarJson(livroResponse)))
                .andExpect(status().isCreated());

        verify(livroService).create(livroRequest);
    }

    @Test
    void deveObterListaDeLivrosMockMvc() throws Exception {
        doReturn(List.of(livro)).when(livroService).getAll();
        doReturn(livroResponse).when(modelMapper).map(any(), any());

        mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(ID.toString()))
                .andExpect(jsonPath("$[0].preco").value(PRECO));

        verify(livroService).getAll();
    }

    @Test
    void deveObterLivroPorIdMockMvc() throws Exception {
        doReturn(livro).when(livroService).getById(any(UUID.class));
        doReturn(livroResponse).when(modelMapper).map(any(Livro.class), any());
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/livros/{id}", ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(gerarJson(livroResponse)))
                .andExpect(status().isOk());

        verify(livroService).getById(ID);
    }

    @Test
    void deveAtualizarLivroMockMvc() throws Exception {
        doReturn(livroResponse).when(modelMapper).map(any(Livro.class), any());
        doReturn(livro).when(livroService).update(any(UUID.class), any(LivroRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/livros/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gerarJson(livroRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().json(gerarJson(livroResponse)))
                .andExpect(status().isOk());

        verify(livroService).update(ID, livroRequest);
    }

    @Test
    void deveRemoverLivroMockMvc() throws Exception {
        String esperado = "Livro cujo Id - %s - foi deletado com sucesso!".formatted(ID);
        doNothing().when(livroService).delete(any(UUID.class));
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/livros/{id}", ID))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(esperado));

        verify(livroService).delete(ID);
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