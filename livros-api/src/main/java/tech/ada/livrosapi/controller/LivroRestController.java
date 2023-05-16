package tech.ada.livrosapi.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.model.dto.LivroResponse;
import tech.ada.livrosapi.service.LivroService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("rest/livros")
public class LivroRestController {

    private final LivroService livroService;
    private final ModelMapper modelMapper;

    public LivroRestController(LivroService livroService, ModelMapper modelMapper) {
        this.livroService = livroService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponse create(@Valid @RequestBody LivroRequest livroRequest){
        return modelMapper.map(livroService.create(livroRequest), LivroResponse.class);
    }

    @GetMapping
    public List<LivroResponse> getAll(){
        return livroService.getAll()
                .stream()
                .map(livro -> modelMapper.map(livro, LivroResponse.class))
                .toList();
    }

    @GetMapping("/{id}")
    public LivroResponse getById(@PathVariable UUID id){
        return modelMapper.map(livroService.getById(id), LivroResponse.class);
    }

    @PutMapping("/{id}")
    public LivroResponse update(@PathVariable UUID id, @Valid @RequestBody LivroRequest livroRequest){
        return modelMapper.map(livroService.update(id, livroRequest), LivroResponse.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        livroService.delete(id);
        return ResponseEntity.ok("Livro cujo Id - %s - foi deletado com sucesso!".formatted(id));
    }
}
