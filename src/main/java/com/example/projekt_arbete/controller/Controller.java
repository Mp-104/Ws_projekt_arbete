package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.ListResponse;
import com.example.projekt_arbete.response.Response;
import com.example.projekt_arbete.service.IFilmService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

// TODO - More error handling, but probably in FilmService class

@RestController
@RequestMapping("/films")
public class Controller {

    @Value("${ApiKey}")
    private String ApiKey;

    //private final FilmRepository filmRepository;


    private final IFilmService filmService;

    private final WebClient webClientConfig;

    private final RateLimiter rateLimiter;

    public Controller (WebClient.Builder webClient, IFilmService filmService, RateLimiter rateLimiter) {
        this.webClientConfig = webClient
                .baseUrl("https://api.themoviedb.org/3/")
                .build();
        //this.filmRepository = repository;
        this.filmService = filmService;
        this.rateLimiter = rateLimiter;
    }

    // TODO - Error handle this shit: internal server error 500 if no film is found - DONE?
    @GetMapping("/{id}")
    public ResponseEntity<Response> getFilmById (@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) {

        try {
            if (rateLimiter.acquirePermission()) {

                Optional<FilmModel> response = Optional.ofNullable(webClientConfig.get()
                        .uri(film -> film
                                .path(movie + "/" + id)
                                .queryParam("api_key", ApiKey)
                                .build())
                        .retrieve()
                        .bodyToMono(FilmModel.class)
                        .block());

                if (response.isPresent()) {
                    return ResponseEntity.ok(response.get());
                }

                return ResponseEntity.status(404).body(new ErrorResponse("Ingen sån film"));
            } else {
                return ResponseEntity.status(429).body(new ErrorResponse("för mycket förfråga"));
            }

        } catch (WebClientResponseException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("Ingen sån film"));
        }

    }

    //TODO - Make sure that films with same name or id cannot be saved, otherwise you can add many of the same films - DONE!
    @PostMapping("/{id}")
    public ResponseEntity<Response> saveFilmById (@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) {

        try {

            if (rateLimiter.acquirePermission()) {
                Optional<FilmModel> response = Optional.ofNullable(webClientConfig.get()
                        .uri(film -> film
                                .path(movie + "/" + id)
                                .queryParam("api_key", ApiKey)
                                .build())
                        .retrieve()
                        .bodyToMono(FilmModel.class)
                        .block());

                if (response.isPresent()) {

                    List<FilmModel> allFilms = filmService.findAll();

                    for (FilmModel film : allFilms) {
                        System.out.println("for each film.getId(): " + film.getId());

                        if (film.getId() == response.get().getId()) {

                            return ResponseEntity.ok(new ErrorResponse("Filmen redan sparad :) "));
                        }

                    }

                    filmService.save(response.get());

                    return ResponseEntity.status(201).body(response.get());
                }

                return ResponseEntity.status(404).body(new ErrorResponse("film inte funnen"));

            } else {
                return ResponseEntity.status(429).body(new ErrorResponse("för mycket förfråga"));
            }

        } catch (WebClientResponseException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("film inte funnen"));
        }

    }

    @GetMapping("/savedfilms")
    public ResponseEntity<Response> getSavedFilms () {

        if (rateLimiter.acquirePermission()) {
            return ResponseEntity.ok(new ListResponse(filmService.findAll()));
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    @PutMapping("/savedfilms/{id}")
    public ResponseEntity<Response> changeCountryOfOrigin (@PathVariable("id") int id, @RequestBody String country) {

        if (rateLimiter.acquirePermission()) {
            return filmService.changeCountryOfOrigin(id, country);
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    @PutMapping("/savedfilms/opinion/{id}")
    public ResponseEntity<String> addOpinion (@PathVariable("id") Integer id, @RequestBody String opinion) {

        if (rateLimiter.acquirePermission()) {
            return filmService.addOpinion(id, opinion);
        } else {
            return ResponseEntity.status(429).body("För många förfrågan");
        }
    }

    @DeleteMapping("/savedfilms/{id}")
    public ResponseEntity<String> deleteFilmById (@PathVariable("id") Integer id) throws Exception {

        if (rateLimiter.acquirePermission()) {
            return filmService.deleteById(id);
        } else {
            return ResponseEntity.status(429).body("för många förfrågan");
        }
    }

    @GetMapping("/savedfilms/runtime")
    public ResponseEntity<Response> getAverageRuntime () {

        if (rateLimiter.acquirePermission()) {
            return filmService.getAverageRuntime();
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    // example url: "https://localhost:8443/films/search?filmName=Reservoir%20Dogs"
    @GetMapping("/search")
    public ResponseEntity<Response> searchByTitle (@RequestParam String filmName) {

        if (rateLimiter.acquirePermission()) {
            return filmService.searchFilmByName(filmName);
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    //Example url: https://localhost:8443/films/country/US?title=Fight%20Club
    @GetMapping("/country/{country}")
    public ResponseEntity<Response> getFilmsByCountry (@PathVariable("country") String country,
                                                       @RequestParam(value = "title", required = false) String title) {

        if (rateLimiter.acquirePermission()) {
            return filmService.getFilmByCountry(country, title);
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }


    @GetMapping("/info")
    public ResponseEntity<Response> getInfo () {

        if (rateLimiter.acquirePermission()) {
            return filmService.getInfo();
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    @GetMapping("/getfilm/{filmId}")
    //https://localhost:8443/films/getfilm/1?opinion=true&description=true example
    public ResponseEntity<Response> getFilmWithAdditionalInfo (@PathVariable("filmId") int filmId,
                                                             @RequestParam(value = "opinion", defaultValue = "false") boolean opinion,
                                                             @RequestParam(value = "description", defaultValue = "false") boolean description) {
        if (rateLimiter.acquirePermission()) {
            return filmService.getFilmWithAdditionalInfo(filmId, opinion, description);
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }


}
