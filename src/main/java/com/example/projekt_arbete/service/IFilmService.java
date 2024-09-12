package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmModel;

import java.util.List;
import java.util.Optional;

//Declare relevant methods
public interface IFilmService {
    FilmModel save (FilmModel film);
    List<FilmModel> findAll ();
    Optional<FilmModel> findById (Integer id);
    void deleteById (Integer id);
}
