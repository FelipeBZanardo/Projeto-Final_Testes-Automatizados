package tech.ada.livrosapi.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.model.dto.LivroResponse;
import tech.ada.livrosapi.service.LivroService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("livros")
public class LivroController {

    private final LivroService livroService;
    private final ModelMapper modelMapper;

    public LivroController(LivroService livroService, ModelMapper modelMapper) {
        this.livroService = livroService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<LivroResponse> create(@Valid @RequestBody LivroRequest livroRequest){
        LivroResponse livroResponse = modelMapper.map(livroService.create(livroRequest), LivroResponse.class);
        return new ResponseEntity<LivroResponse>(livroResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<LivroResponse>> getAll(){
        List<LivroResponse> responseList = livroService.getAll()
                .stream()
                .map(livro -> modelMapper.map(livro, LivroResponse.class))
                .toList();
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> getById(@PathVariable UUID id){
        LivroResponse livroResponse = modelMapper.map(livroService.getById(id), LivroResponse.class);
        return ResponseEntity.ok(livroResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> update(@PathVariable UUID id, @Valid @RequestBody LivroRequest livroRequest){
        LivroResponse livroResponse = modelMapper.map(livroService.update(id, livroRequest), LivroResponse.class);
        return ResponseEntity.ok(livroResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        livroService.delete(id);
        return ResponseEntity.ok("Livro cujo Id - %s - foi deletado com sucesso!".formatted(id));
    }
}
