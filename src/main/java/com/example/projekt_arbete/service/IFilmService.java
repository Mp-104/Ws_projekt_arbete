package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

//Declare relevant methods
public interface IFilmService {
    FilmModel save (FilmModel film);
    List<FilmModel> findAll ();
    ResponseEntity<Response> findById (Integer id);
    ResponseEntity<String> deleteById (Integer id) throws Exception;
    ResponseEntity<Response> changeCountryOfOrigin (@PathVariable("id") int id, @RequestBody String country);
    ResponseEntity<Response> searchFilmByName (String filmName);
    ResponseEntity<Response> getFilmByCountry (String country, String title);
    ResponseEntity<Response> getAverageRuntime ();
    ResponseEntity<String> addOpinion (Integer id, String opinion);
}
