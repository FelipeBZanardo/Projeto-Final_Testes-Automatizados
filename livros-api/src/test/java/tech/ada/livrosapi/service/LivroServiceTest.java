package tech.ada.livrosapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.ada.livrosapi.exception.BookNotFoundException;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.repository.LivroRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
    @InjectMocks
    private LivroService livroService;
    @Mock
    private LivroRepository livroRepository;
    private Livro livro;
    private LivroRequest livroRequest;
    private static final UUID ID = UUID.fromString("5fa3cf7a-2836-40dc-957b-43bdac98104a");
    private static final String TITULO = "Título";
    private static final String RESUMO = "Resumo";
    private static final String SUMARIO = "Sumário";
    private static final BigDecimal PRECO = BigDecimal.valueOf(20.00);
    private static final int NUMERO_PAGINAS = 100;
    private static final String ISBN = "1234567891011";
    private static final LocalDate DATA_PUBLICACAO = LocalDate.of(2023, 6, 25);

    @BeforeEach
    void setUp() {
        startLivro();
    }

    @Test
    void deveCriarUmLivroTest() {
        doReturn(livro).when(livroRepository).save(any(Livro.class));
        Livro resultado = livroService.create(livroRequest);
        assertEquals(livro.getTitulo(), resultado.getTitulo());
    }

    @Test
    void deveObterListaDeLivrosTest() {
        Livro livro1 = new Livro(UUID.randomUUID(), TITULO + " 1", RESUMO  + " 1", SUMARIO  + " 1",
                PRECO.add(new BigDecimal("10.00")), NUMERO_PAGINAS + 100, "234567891011",
                DATA_PUBLICACAO.plusYears(1));
        Livro livro2 = new Livro(UUID.randomUUID(), TITULO + " 2", RESUMO  + " 2", SUMARIO  + " 2",
                PRECO.add(new BigDecimal("20.00")), NUMERO_PAGINAS + 200, "3456789101112",
                DATA_PUBLICACAO.plusYears(2));

        List<Livro> esperado = List.of(livro, livro1, livro2);
        doReturn(esperado).when(livroRepository).findAll();
        List<Livro> resultado = livroService.getAll();

        assertEquals(esperado, resultado);
    }

    @Test
    void deveObterLivroPorIdTest() {
        doReturn(Optional.of(livro)).when(livroRepository).findById(any(UUID.class));
        Livro resultado = livroService.getById(ID);
        assertEquals(livro.getTitulo(), resultado.getTitulo());
    }

    @Test
    void deveObterExceptionPorIdInexistenteNaCriacaoDoLivroTest() {
        doThrow(new BookNotFoundException(ID)).when(livroRepository).findById(any(UUID.class));
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class, () -> livroService.getById(ID));
        assertEquals(String.format("ID %s não existe na base de dados", ID), bookNotFoundException.getMessage());
    }

    @Test
    void deveAtualizarLivroTest() {
        doReturn(Optional.of(livro)).when(livroRepository).findById(any(UUID.class));
        doReturn(livro).when(livroRepository).save(any(Livro.class));
        Livro resultado = livroService.update(ID, livroRequest);
        assertEquals(livro, resultado);
    }

    @Test
    void deveObterExceptionPorIdInexistenteNaAtualizacaoDoLivroTest() {
        doThrow(new BookNotFoundException(ID)).when(livroRepository).findById(any(UUID.class));
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
                () -> livroService.update(ID, livroRequest));
        assertEquals(String.format("ID %s não existe na base de dados", ID), bookNotFoundException.getMessage());
    }

    @Test
    void deveDeletarLivroTest() {
        doReturn(Optional.of(livro)).when(livroRepository).findById(any(UUID.class));
        doNothing().when(livroRepository).deleteById(any(UUID.class));
        livroService.delete(ID);
        verify(livroRepository).deleteById(ID);
    }

    @Test
    void deveObterExceptionPorIdInexistenteNaRemocaoDoLivroTest() {
        doThrow(new BookNotFoundException(ID)).when(livroRepository).findById(any(UUID.class));
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class,
                () -> livroService.delete(ID));
        assertEquals(String.format("ID %s não existe na base de dados", ID), bookNotFoundException.getMessage());
    }

    private void startLivro(){
        livro = new Livro(ID, TITULO, RESUMO, SUMARIO, PRECO, NUMERO_PAGINAS, ISBN, DATA_PUBLICACAO);
        livroRequest = new LivroRequest(TITULO, RESUMO, SUMARIO, PRECO, NUMERO_PAGINAS, ISBN, DATA_PUBLICACAO);
    }
}