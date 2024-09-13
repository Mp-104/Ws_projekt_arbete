package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
}
