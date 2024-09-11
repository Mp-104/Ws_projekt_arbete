package com.example.projekt_arbete.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/films")
public class Controller {

    private String ApiKey;

    private final WebClient webClientConfig;

    public Controller (WebClient.Builder webClient) {
        this.webClientConfig = webClient
                .baseUrl("https://api.themoviedb.org/3/movie/550?api_key=" + ApiKey)
                .build();
    }

    @GetMapping
    public Mono<String> getFilm () {

        return webClientConfig.get()
                .retrieve()
                .bodyToMono(String.class);
    }

}
