package com.example.projekt_arbete.repository;

import com.example.projekt_arbete.model.FilmModel;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Mono;

@org.springframework.stereotype.Repository
public interface FilmRepository extends JpaRepository<FilmModel, Integer> {
}
