package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public void deleteById (Integer id) {

        assert filmRepository.findById(id).isPresent();

        //filmRepository.findById(id).get();

        filmRepository.deleteById(id);

    }
}
