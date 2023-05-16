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
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "", "Sumario 1",
                new BigDecimal("20.00"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O Resumo do livro é obrigatório", messageViolation);
    }

    @Test
    void resumoNaoDeveSerNullTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", null, "Sumario 1",
                new BigDecimal("20.00"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O Resumo do livro é obrigatório", messageViolation);
    }

    @Test
    void resumoNaoDeveTerMaisQue500CaracteresTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", obterResumoInvalido(), "Sumario 1",
                new BigDecimal("20.00"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O resumo deve conter no máximo 500 caracteres", messageViolation);
    }

    @Test
    void sumarioNaoDeveSerNullTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", null,
                new BigDecimal("20.00"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O Sumário do livro é obrigatório", messageViolation);
    }

    @Test
    void precoNaoDeveSerNullTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                null, 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O Preço do livro é obrigatório", messageViolation);
    }

    @Test
    void precoNaoDeveSerMenorQue20Test() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("19.99"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O valor mínimo é de R$20,00", messageViolation);
    }

    @Test
    void precoDeveSerValorMonetarioTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("25.0123"), 100, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O Preço deve ser um valor monetário.", messageViolation);
    }

    @Test
    void numeroPaginasNaoDeveSerNullTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), null, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O Número de Páginas do livro é obrigatório", messageViolation);
    }

    @Test
    void numeroPaginasNaoDeveSerMenorQue100Test() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), 99, "12345678910110",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("O número mínimo de páginas é 100", messageViolation);
    }

    @Test
    void isbnNaoDeverSerNullTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), 100, null,
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("ISBN do livro é obrigatório", messageViolation);
    }

    @Test
    void isbnNaoDeverSerVazioTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), 100, "",
                LocalDate.of(2023, 6, 25));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("ISBN do livro é obrigatório", messageViolation);
    }

    @Test
    void dataPublicacaoNaoDeveSerNullTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), 100, "12345678910110",
                null);
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("A Data de Publicação do livro é obrigatória", messageViolation);
    }

    @Test
    void dataPublicacaoDeveSerNoFuturoTest() {
        LivroRequest livroRequest = new LivroRequest("Titulo 1", "Resumo 1", "Sumario 1",
                new BigDecimal("20.00"), 100, "12345678910110",
                LocalDate.now().minusDays(1L));
        Set<ConstraintViolation<LivroRequest>> violations = getConstraintViolations(livroRequest);
        String messageViolation = getMessageViolation(violations);
        assertFalse(violations.isEmpty());
        assertEquals("A Data de Publicação do livro deve ser no fututo", messageViolation);
    }

    private String obterResumoInvalido(){
        //Mais de 500 caracteres
        return "A guerra é um assunto de importância vital para o Estado […]”." +
                " Com essa frase, o livro A Arte da Guerra inicia." +
                " Ela pode ser compreendida, fora de seu contexto, como “a batalha é essencial para o Estado”. " +
                "Certamente, um ponto que Sun Tzu afirma durante o livro é a guerra ser diferente do campo de batalha. " +
                "O livro, pois, não trata sobre como batalhar, e sim de estratégias de liderança para generais.\n" +
                "A Arte da Guerra é um livro do século quatro antes de Cristo, escrito por Sun Tzu, " +
                "um estrategista militar chinês. " +
                "O livro é dividido em 13 capítulos dedicados a assuntos da guerra " +
                "ele é escrito em aforismos do Sun Tzu com comentários de outros generais e " +
                "estrategistas que contam exemplos e histórias sobre os aforismos. ";
    }

    private static String getMessageViolation(Set<ConstraintViolation<LivroRequest>> violations) {
        return violations.stream().findFirst().orElseThrow().getMessage();
    }

    private Set<ConstraintViolation<LivroRequest>> getConstraintViolations(LivroRequest livroRequest) {
        return validator.validate(livroRequest);
    }
}