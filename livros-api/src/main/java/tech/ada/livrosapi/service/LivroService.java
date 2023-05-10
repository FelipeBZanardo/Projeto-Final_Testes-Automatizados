package tech.ada.livrosapi.service;

import org.springframework.stereotype.Service;
import tech.ada.livrosapi.exception.BookNotFoundException;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.repository.LivroRepository;

import java.util.List;
import java.util.UUID;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro create(LivroRequest livroRequest) {
        return livroRepository.save(buildLivro(livroRequest));
    }
    private Livro buildLivro(LivroRequest livroRequest){
        return Livro.builder()
                .titulo(livroRequest.titulo())
                .resumo(livroRequest.resumo())
                .sumario(livroRequest.sumario())
                .preco(livroRequest.preco())
                .numeroPaginas(livroRequest.numeroPaginas())
                .isbn(livroRequest.isbn())
                .dataPublicacao(livroRequest.dataPublicacao())
                .build();
    }

    public List<Livro> getAll() {
        return livroRepository.findAll();
    }

    public Livro getById(UUID id) {
        return livroRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));

    }

    public Livro update(UUID id, LivroRequest livroRequest) {
        getById(id);
        Livro livroAtualizado = buildLivro(livroRequest);
        return livroRepository.save(livroAtualizado.withId(id));

    }

    public void delete(UUID id) {
        getById(id);
        livroRepository.deleteById(id);
    }
}
