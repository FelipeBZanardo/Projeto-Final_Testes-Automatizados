package tech.ada.livrosapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.service.LivroService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class LivroControllerTest {
    @InjectMocks
    private LivroController livroController;
    @Mock
    private LivroService livroService;
    private MockMvc mockMvc;
    private Livro livro;
    private static final UUID ID = UUID.fromString("5fa3cf7a-2836-40dc-957b-43bdac98104a");
    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(livroController).build();
        startLivro();
    }

    @Test
    void deveDirecionarParaPaginaDeListarLivros() throws Exception {
        doReturn(List.of(livro)).when(livroService).getAll();
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/livros/"))
                .andExpect(status().isOk())
                .andExpect(view().name("livros"))
                .andExpect(model().attributeExists("livros"));
    }

    @Test
    void deveRedirecionarParaPaginaDeCadastrarLivro() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/livros/cadastrar"))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastrar-livro"))
                .andExpect(model().attributeExists("livro"));
    }

   @Test
    void deveRedirecionarParaPaginaDeLivrosAposSalvar() throws Exception {
        doReturn(livro).when(livroService).create(any(LivroRequest.class));
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/livros/cadastrar")
                        .param("titulo", "Titulo")
                        .param("resumo", "Resumo")
                        .param("sumario", "Sumário")
                        .param("preco", "20.00")
                        .param("numeroPaginas", "100")
                        .param("isbn", "2121654541")
                        .param("dataPublicacao", "2023-12-12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/livros"));
    }

    @Test
    void deveRedirecionarParaPaginaDeLivrosAposRemocao() throws Exception {
        doNothing().when(livroService).delete(any(UUID.class));
        this.mockMvc
                .perform(MockMvcRequestBuilders.delete("/livros/{id}", ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/livros"));
    }

    @Test
    void deveDirecionarParaPaginaDeEdicaoDeLivros() throws Exception {
        doReturn(livro).when(livroService).getById(any(UUID.class));
        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/livros/editar/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(view().name("editar-livro"))
                .andExpect(model().attributeExists("livro"));
    }

    @Test
    void deveRedirecionarParaPaginaDeLivrosAposEdicao() throws Exception {
        doReturn(livro).when(livroService).update(any(UUID.class), any(LivroRequest.class));
        this.mockMvc
                .perform(MockMvcRequestBuilders.put("/livros/editar/{id}", ID)
                        .param("titulo", "Titulo")
                        .param("resumo", "Resumo")
                        .param("sumario", "Sumário")
                        .param("preco", "20.00")
                        .param("numeroPaginas", "100")
                        .param("isbn", "2121654541")
                        .param("dataPublicacao", "2023-12-12"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/livros"));
    }

    private void startLivro(){
        String titulo = "Titulo";
        String resumo = "Resumo";
        String sumario = "Sumario";
        BigDecimal preco = BigDecimal.valueOf(20.00);
        Integer numeroPaginas = 100;
        String isbn = "1234567891011";
        LocalDate dataPublicacao = LocalDate.of(2023,6,25);
        livro = new Livro(ID, titulo, resumo, sumario, preco, numeroPaginas, isbn, dataPublicacao);
    }
}