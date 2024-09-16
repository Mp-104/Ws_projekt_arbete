package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// Do more error handling
@Service
public class FilmService implements IFilmService{

    @Autowired
    private FilmRepository filmRepository;

    @Override
    public FilmModel save (FilmModel film) {
        return filmRepository.save(film);
    }

    @Override
    public List<FilmModel> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<FilmModel> findById(Integer id) {
        return filmRepository.findById(id);
    }

    @Override
    public ResponseEntity<String> deleteById (Integer id) throws Exception {

        assert filmRepository.findById(id).isPresent();
        try {
            if (filmRepository.findById(id).isPresent()) {
                filmRepository.deleteById(id);
                return ResponseEntity.ok("Film with id "+ id + " Deleted");
            } else {
                //throw new Exception("No film found with id: " + id);
                return ResponseEntity.status(404).body("no film found with id: " + id);
            }
        } catch (Exception e) {
            throw new Exception();
        }
        //filmRepository.findById(id).get();
    }

    @Override
    public ResponseEntity<Response> changeCountryOfOrigin(@PathVariable("id") int id, @RequestBody String country) {

        List<String> newCountryOfOrigins = new ArrayList<>() {};

        newCountryOfOrigins.add(country);

        Optional<FilmModel> filmOptional = filmRepository.findById(id);

        if (filmOptional.isEmpty()) {
            return ResponseEntity.status(400).body(new ErrorResponse("Film finns inte! <@:)"));
        }

        try {

            FilmModel film = filmOptional.get();

            film.setOrigin_country(newCountryOfOrigins);

            filmRepository.save(film);

            return ResponseEntity.ok(film);

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(404).body(new ErrorResponse("film finns inte"));
        }

    }

}
