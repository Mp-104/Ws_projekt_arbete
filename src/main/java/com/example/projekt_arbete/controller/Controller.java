package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.Keys;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import com.example.projekt_arbete.service.IFilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class Controller {

    private final FilmRepository filmRepository;

    private final IFilmService filmService;

    private final WebClient webClientConfig;

    public Controller (WebClient.Builder webClient, IFilmService filmService, FilmRepository repository) {
        this.webClientConfig = webClient
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
        this.filmRepository = repository;
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
    public ResponseEntity<Optional<FilmModel>> saveFilmById (@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) {

        //Optional
        Optional<FilmModel> response = Optional.ofNullable(webClientConfig.get()
                .uri(film -> film
                        .path(movie + "/" + id)
                        .queryParam("api_key", Keys.ApiKey)
                        .build())
                .retrieve()
                .bodyToMono(FilmModel.class)
                .block());


        // Suggested by IntelliJ, ingen aning hur det fungerar
        assert response.isPresent();

        filmService.save(response.get());

        return ResponseEntity.status(201).body(response);

    }

    @GetMapping("/savedfilms")
    public ResponseEntity<List<FilmModel>> getSavedFilms () {

        //return filmRepository.findAll();
        return ResponseEntity.ok(filmService.findAll());
    }

    @PutMapping ("/savedfilms/{id}")
    public ResponseEntity<FilmModel> changeCountryOfOrigin (@PathVariable("id") int id, @RequestBody String country) {

        //return filmRepository.findAll();
        List<FilmModel> filmList = filmService.findAll();


        List<String> newCountryOfOrigins = new ArrayList<>() {};

        newCountryOfOrigins.add(country);

        //filmList.get(id).setOrigin_country(newCountryOfOrigins);

        //FilmModel film = filmList.get(id);

        filmService.findById(id).get().setOrigin_country(newCountryOfOrigins);


        filmService.save(filmService.findById(id).get());

        return ResponseEntity.status(200).body(filmService.findById(id).get());
        //return filmService.save(film);

    }

    @DeleteMapping("/savedfilms/{id}")
    public ResponseEntity<String> deleteFilmById (@PathVariable("id") Integer id) throws Exception {
        try {
            filmService.deleteById(id);

            return ResponseEntity.ok("Film with id "+ id + " Deleted");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("No film with id:" + id + " found");
        }


    }



}
