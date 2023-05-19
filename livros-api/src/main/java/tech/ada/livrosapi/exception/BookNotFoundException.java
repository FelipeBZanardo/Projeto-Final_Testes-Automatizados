package tech.ada.livrosapi.exception;
import java.util.UUID;

public class BookNotFoundException extends RuntimeException{

    private final UUID id;
    public BookNotFoundException(UUID id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("ID %s não existe na base de dados", id);
    }

}
