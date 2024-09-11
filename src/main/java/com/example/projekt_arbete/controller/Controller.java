package com.example.projekt_arbete.controller;

import com.example.projekt_arbete.Keys;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/films")
public class Controller {

    String url = String.format("https://api.themoviedb.org/3/movie/550?api_key=%s", Keys.ApiKey);
    //"https://api.themoviedb.org/3/movie/" + 540 + "?api_key=" + Keys.ApiKey
    private final WebClient webClientConfig;

    public Controller (WebClient.Builder webClient) {
        this.webClientConfig = webClient
                .baseUrl("https://api.themoviedb.org/3/movie")
                .build();
    }

    @GetMapping("/{id}")
    public Mono<String> getFilmById (@PathVariable int id) {

        return webClientConfig.get()
                .uri(film -> film
                        .path("/" + id)
                        .queryParam("api_key", Keys.ApiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

}
