package tech.ada.livrosapi.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.*;
import tech.ada.livrosapi.system.LocalDateDeserializer;
import tech.ada.livrosapi.system.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
public record LivroRequest(

        @NotBlank(message = "O Título do livro é obrigatório")
        String titulo,
        @NotBlank(message = "O Resumo do livro é obrigatório")
        @Size(max = 500, message = "O resumo deve conter no máximo 500 caracteres")
        String resumo,
        @NotNull(message = "O Sumário do livro é obrigatório")
        String sumario,
        @NotNull(message = "O Preço do livro é obrigatório")
        @DecimalMin(value = "20.00", message = "O valor mínimo é de R$20,00")
        @Digits(integer = 10,fraction = 2, message = "O Preço deve ser um valor monetário.")
        BigDecimal preco,
        @NotNull(message = "O Número de Páginas do livro é obrigatório")
        @Min(value = 100, message = "O número mínimo de páginas é 100")
        Integer numeroPaginas,
        @NotBlank(message = "ISBN do livro é obrigatório")
        String isbn,
        //@JsonFormat(pattern = "dd/MM/yyyy")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @Future(message = "A Data de Publicação do livro deve ser no fututo")
        @NotNull(message = "A Data de Publicação do livro é obrigatória")
        LocalDate dataPublicacao) {

}
