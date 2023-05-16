package tech.ada.livrosapi.controller;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.ada.livrosapi.model.Livro;
import tech.ada.livrosapi.model.dto.LivroRequest;
import tech.ada.livrosapi.model.dto.LivroResponse;
import tech.ada.livrosapi.service.LivroService;

import java.util.List;
import java.util.UUID;


@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public String getAll(Model model){
        List<Livro> livros = livroService.getAll();
        model.addAttribute("livros", livros);
        return "livros";
    }

    @GetMapping("/cadastrar")
    public String create(Model model){
        model.addAttribute("livro", new Livro());
        return "cadastrar-livro";
    }

    @PostMapping("/cadastrar")
    public String save(@Valid @RequestBody LivroRequest livroRequest){
        livroService.create(livroRequest);
        return "redirect:/livros";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id){
        livroService.delete(UUID.fromString(id));
        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable String id, Model model) {
        Livro livro = livroService.getById(UUID.fromString(id));
        model.addAttribute("livro", livro);
        return "editar-livro";
    }

    @PutMapping("/editar/{id}")
    public String update(@PathVariable String id, @Valid @RequestBody LivroRequest livroRequest) {
        livroService.update(UUID.fromString(id), livroRequest);
        return "redirect:/livros";
    }

}
