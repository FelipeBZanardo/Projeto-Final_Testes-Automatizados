package tech.ada.livrosapi.model.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LivroRequestTest {
    private Validator validator;
    private ValidatorFactory factory;
    @BeforeEach
    void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @AfterEach
    void tearDown() {
        factory.close();
    }
    @Test
    void tituloNaoDeveSerVazioTest() {
        LivroRequest livroRequest = new LivroRequest("", "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));

        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);

        assertFalse(violations.isEmpty());
        assertEquals("O Título do livro é obrigatório", messageViolation);
    }

    @Test
    void tituloNaoDeveSerNullTest() {
        LivroRequest livroRequest = new LivroRequest(null, "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O Título do livro é obrigatório", messageViolation);
    }

    @Test
    void resumoNaoDeveSerVazioTest() {
    }
    @Test
    void resumoNaoDeveSerNullTest() {
    }

    @Test
    void resumoNaoDeveTerMaisQue500CaracteresTest() {
    }

    @Test
    void sumarioNaoDeveSerNullTest() {
    }

    @Test
    void precoNaoDeveSerNullTest() {
    }

    @Test
    void precoNaoDeveSerMenorQue20Test() {
    }

    @Test
    void precoDeveSerValorMonetarioTest() {
    }

    @Test
    void numeroPaginasNaoDeveSerNullTest() {
    }

    @Test
    void numeroPaginasNaoDeveSerMenorQue100Test() {
    }

    @Test
    void isbnNaoDeverSerNullTest() {
    }

    @Test
    void isbnNaoDeverSerVazioTest() {

    }

    @Test
    void dataPublicacaoNaoDeveSerNullTest() {
    }

    @Test
    void dataPublicacaoDeveSerNoFuturoTest() {
    }

    private static String getMessageViolation(Set<ConstraintViolation<LivroRequest>> violations) {
        return violations.stream().findFirst().orElseThrow().getMessage();
    }

    private Set<ConstraintViolation<LivroRequest>> getConstraintViolations(LivroRequest livroRequest) {
        return validator.validate(livroRequest);
    }
}