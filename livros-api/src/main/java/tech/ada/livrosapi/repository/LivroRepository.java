package tech.ada.livrosapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ada.livrosapi.model.Livro;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
}
