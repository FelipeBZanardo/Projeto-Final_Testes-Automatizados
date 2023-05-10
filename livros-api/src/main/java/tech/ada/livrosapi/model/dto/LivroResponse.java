package tech.ada.livrosapi.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.ada.livrosapi.system.LocalDateDeserializer;
import tech.ada.livrosapi.system.LocalDateSerializer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroResponse {
        private UUID id;
        private String titulo;
        private String resumo;
        private String sumario;
        private BigDecimal preco;
        private int numeroPaginas;
        private String isbn;
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonFormat(pattern = "dd/MM/yyyy")
        private LocalDate dataPublicacao;
}
