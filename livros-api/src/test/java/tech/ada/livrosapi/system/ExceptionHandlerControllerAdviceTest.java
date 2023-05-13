package tech.ada.livrosapi.system;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import tech.ada.livrosapi.exception.BookNotFoundException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerAdviceTest {
    @InjectMocks
    private ExceptionHandlerControllerAdvice exceptionHandlerControllerAdvice;
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    @Mock
    private BindingResult bindingResult;
    private final UUID ID = UUID.randomUUID();

    @Test
    void handleBookNotFoundExceptionTest() {
        ResponseEntity<String> response = exceptionHandlerControllerAdvice
                .handleBookNotFoundException(new BookNotFoundException(ID));

        assertEquals("ID %s não existe na base de dados".formatted(ID), response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleMethodArgumentNotValidTest() {
        String objectName = "objectName";
        String field = "field";
        String defaultMessage = "defaultMessage";

        String mensagemEsperada = gerarMensagemEsperada(objectName, field, defaultMessage);
        List<FieldError> fieldErrors = gerarListaErros(objectName, field, defaultMessage);


        doReturn(bindingResult).when(methodArgumentNotValidException).getBindingResult();
        doReturn(fieldErrors).when(bindingResult).getFieldErrors();
        doReturn(fieldErrors).when(bindingResult).getGlobalErrors();

        ResponseEntity<Object> response = exceptionHandlerControllerAdvice
                .handleMethodArgumentNotValid(methodArgumentNotValidException, mock(HttpHeaders.class),
                        HttpStatus.BAD_REQUEST, mock(WebRequest.class));

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagemEsperada, response.getBody());
    }

    private List<FieldError> gerarListaErros(String objectName, String field, String defaultMessage) {
        return List.of(new FieldError(objectName, field, defaultMessage));
    }

    private String gerarMensagemEsperada(String objectName, String field, String defaultMessage) {
        return "Campo inválido: '%s'. Causa: '%s'.\r\n".formatted(field, defaultMessage) +
                ", Campo inválido: '%s'. Causa: '%s'.\r\n".formatted(objectName, defaultMessage);
    }
}