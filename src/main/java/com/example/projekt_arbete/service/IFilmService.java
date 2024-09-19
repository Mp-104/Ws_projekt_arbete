package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

//Declare relevant methods
public interface IFilmService {
    FilmModel save (FilmModel film);
    List<FilmModel> findAll ();
    ResponseEntity<Response> findById (Integer id);
    ResponseEntity<String> deleteById (Integer id) throws Exception;
    ResponseEntity<Response> changeCountryOfOrigin (int id, String country);
    ResponseEntity<Response> searchFilmByName (String filmName);
    ResponseEntity<Response> getFilmByCountry (String country, String title);
    ResponseEntity<Response> getAverageRuntime ();
    ResponseEntity<String> addOpinion (Integer id, String opinion);
    ResponseEntity<Response> getFilmWithAdditionalInfo(int filmId, boolean opinion, boolean description);
    ResponseEntity<Response> getInfo();
}
