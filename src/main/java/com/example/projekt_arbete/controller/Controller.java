package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.IntegerResponse;
import com.example.projekt_arbete.response.Response;
import com.example.projekt_arbete.service.IFilmService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

                assert response.isPresent();
                return ResponseEntity.ok(response.get());
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


                //if (response.isEmpty()) {
                //    return ResponseEntity.status(404).body(new ErrorResponse("film inte hittad"));
                //}
                // Suggested by IntelliJ, ingen aning hur det fungerar
                assert response.isPresent();

                List<FilmModel> allFilms = filmService.findAll();

                for (FilmModel film : allFilms) {
                    System.out.println("for each film.getId(): " + film.getId());

                    if (film.getId() == response.get().getId()) {

                        return ResponseEntity.ok(new ErrorResponse("Filmen redan sparad :) "));
                    }

                }

                filmService.save(response.get());

                return ResponseEntity.status(201).body(response.get());
            } else {
                return ResponseEntity.status(429).body(new ErrorResponse("för mycket förfråga"));
            }

        } catch (WebClientResponseException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("film inte funnen"));
        }

    }

    @GetMapping("/savedfilms")
    public ResponseEntity<List<FilmModel>> getSavedFilms () {

        return ResponseEntity.ok(filmService.findAll());
    }

    //TODO - needs more work/error handling Optional?
    @PutMapping("/savedfilms/{id}")
    public ResponseEntity<Response> changeCountryOfOrigin (@PathVariable("id") int id, @RequestBody String country) {

        return filmService.changeCountryOfOrigin(id, country);

        //List<String> newCountryOfOrigins = new ArrayList<>() {};
        //newCountryOfOrigins.add(country);
        //assert filmService.findById(id).isPresent();
        //filmService.findById(id).get().setOrigin_country(newCountryOfOrigins);
        //filmService.save(filmService.findById(id).get());
        //return ResponseEntity.ok(filmService.findById(id).get());
    }

    @PutMapping("/savedfilms/opinion/{id}")
    public ResponseEntity<String> addOpinion (@PathVariable("id") Integer id, @RequestBody String opinion) {

        return filmService.addOpinion(id, opinion);

    }

    @DeleteMapping("/savedfilms/{id}")
    public ResponseEntity<String> deleteFilmById (@PathVariable("id") Integer id) throws Exception {
        return filmService.deleteById(id);

//        try {
//
//
//            return ResponseEntity.ok("Film with id "+ id + " Deleted");
//        } catch (Exception e) {
//            return ResponseEntity.status(404).body("No film with id:" + id + " found");
//        }

    }

    //TODO - Error handle 500 internal error cannot divide by 0 zero DONE!
    @GetMapping("/savedfilms/runtime")
    public ResponseEntity<Response> getAverageRuntime () {

        return filmService.getAverageRuntime();
    }

    // example url: "https://localhost:8443/films/search?filmName=Reservoir%20Dogs"
    @GetMapping("/search")
    public ResponseEntity<Response> searchByTitle (@RequestParam String filmName) {

       return filmService.searchFilmByName(filmName);
    }

    //TODO - Error handle and move code to relevant FilmService method
    //Example url: https://localhost:8443/films/country/US?title=Fight%20Club
    @GetMapping("/country/{country}")
    public ResponseEntity<Response> getFilmsByCountry (@PathVariable("country") String country,
                                                       @RequestParam(value = "title", required = false) String title) {

        return filmService.getFilmByCountry(country, title);
    }

    @GetMapping("/info")
    public ResponseEntity<Response> getInfo () {

        ArrayList<FilmModel> adultFilms = new ArrayList<>();
        ArrayList<String> budgetFilms = new ArrayList<>();

        List<FilmModel> films = filmService.findAll();
        Collections.sort(films, new Comparator<FilmModel>() {
            @Override
            public int compare(FilmModel o1, FilmModel o2) {
                return Integer.compare(o1.getBudget(), o2.getBudget());
            }
        });

        for (FilmModel film : films) {
            System.out.println(film.getOriginal_title() + ": " + film.getBudget());
            budgetFilms.add(film.getOriginal_title() + " " + film.getBudget());
        }

        for (FilmModel film : filmService.findAll()) {
            if (film.isAdult() == true) {
                adultFilms.add(film);

            }
        }

        if (filmService.findAll().isEmpty()) {
            return ResponseEntity.ok(new ErrorResponse("Du har inga sparade filmer"));
        }


        IntegerResponse intRes = (IntegerResponse) filmService.getAverageRuntime().getBody();
        int y = intRes.getAverageRuntime();

        return ResponseEntity.ok(new ErrorResponse("du har: " + filmService.findAll().size() + " filmer sparade." +
                " medellängden på filmerna är: " + y + " minuter, " +
                "varav " + adultFilms.size() + " porrfilm(er)" + "budge rank " + budgetFilms));

    }


}
