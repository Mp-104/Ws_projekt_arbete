package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.Keys;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import com.example.projekt_arbete.service.IFilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/films")
public class Controller {

    //private final FilmRepository filmRepository;

    private final IFilmService filmService;

    private final WebClient webClientConfig;

    public Controller (WebClient.Builder webClient, IFilmService filmService, FilmRepository repository) {
        this.webClientConfig = webClient
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
        //this.filmRepository = repository;
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<FilmModel>> getFilmById (@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) {

        Mono<FilmModel> response = webClientConfig.get()
                .uri(film -> film
                        .path(movie + "/" + id)
                        .queryParam("api_key", Keys.ApiKey)
                        .build())
                .retrieve()
                .bodyToMono(FilmModel.class);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/{id}")
    public ResponseEntity<FilmModel> saveFilmById (@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) {

        FilmModel response = webClientConfig.get()
                .uri(film -> film
                        .path(movie + "/" + id)
                        .queryParam("api_key", Keys.ApiKey)
                        .build())
                .retrieve()
                .bodyToMono(FilmModel.class)
                .block();


        // Suggested by IntelliJ, ingen aning hur det fungerar
        assert response != null;

        filmService.save(response);

        return ResponseEntity.status(201).body(response);

    }

}
