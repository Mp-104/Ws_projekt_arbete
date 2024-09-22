package com.example.projekt_arbete.service;

import com.example.projekt_arbete.model.FilmDTO;
import com.example.projekt_arbete.model.FilmModel;
import com.example.projekt_arbete.repository.FilmRepository;
import com.example.projekt_arbete.response.ErrorResponse;
import com.example.projekt_arbete.response.IntegerResponse;
import com.example.projekt_arbete.response.ListResponse;
import com.example.projekt_arbete.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<FilmModel> findAll () {
        return filmRepository.findAll();
    }

    @Override
    public ResponseEntity<Response> findById (Integer id) {

        try {

            Optional<FilmModel> optionalFilm = filmRepository.findById(id);

            if (optionalFilm.isPresent()) {

                return ResponseEntity.ok((optionalFilm.get()));
            } else {

                return ResponseEntity.status(404).body(new ErrorResponse("film finns inte"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }

    }

    @Override
    public ResponseEntity<String> deleteById (Integer id) throws Exception {

        Optional<FilmModel> optionalFilm = filmRepository.findById(id);

        try {

            if (filmRepository.findById(id).isPresent()) {

                filmRepository.deleteById(id);
                return ResponseEntity.ok("Film med id "+ id + " tagen borta");

            } else {

                return ResponseEntity.status(404).body("no film found with id: " + id);
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public ResponseEntity<Response> changeCountryOfOrigin (int id, String country) {

        List<String> newCountryOfOrigins = new ArrayList<>() {};

        newCountryOfOrigins.add(country);

        Optional<FilmModel> filmOptional = filmRepository.findById(id);

        if (filmOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Film finns inte! <@:)"));
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

    @Override
    public ResponseEntity<Response> searchFilmByName (String filmName) {

        try {

            if (filmName == null || filmName.isBlank()) {
                return ResponseEntity.status(400).body(new ErrorResponse("Du måste skriva namn"));
            }

            List<FilmModel> allFilms = filmRepository.findAll();

            for (FilmModel film : allFilms) {
                //System.out.println(film.getOriginal_title());

                if (film.getOriginal_title().equals(filmName)) {

                    return ResponseEntity.ok(film);
                }
            }

            return ResponseEntity.status(404).body(new ErrorResponse("Ingen film funnen med namn: " + filmName));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<Response> getFilmByCountry (String country, String title) {

        List<FilmModel> savedFilms = filmRepository.findAll();

        List<FilmModel> filmsByCountry = new ArrayList<>();

        try {


            if (title == null || title.isBlank()) {

                for (FilmModel film : savedFilms) {

                    if (film.getOrigin_country().get(0).equals(country.toUpperCase())) {

                        filmsByCountry.add(film);
                    }
                }

                return ResponseEntity.ok(new ListResponse(filmsByCountry));
            }

            for (FilmModel film : savedFilms) {

                if (film.getOrigin_country().get(0).equals(country.toUpperCase()) && film.getOriginal_title().equals(title)) {

                    return ResponseEntity.ok(film);
                }
            }

            return ResponseEntity.status(400).body(new ErrorResponse("Finns inte film: " + title));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<Response> getAverageRuntime () {

        try {

            List<FilmModel> films = filmRepository.findAll();
            if (films.isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("inga filmer sparade än"));
            }

            int runtimeInMin = 0;

            for (FilmModel film : films) {

                runtimeInMin += film.getRuntime();

            }

            return ResponseEntity.ok(new IntegerResponse(runtimeInMin / filmRepository.findAll().size()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<String> addOpinion (Integer id, String opinion) {

        try {
            if (opinion == null || opinion.isEmpty() || opinion.isBlank()) {
                return ResponseEntity.status(400).body("måste ha body");
            }

            Optional<FilmModel> optionalFilm = filmRepository.findById(id);

            if (optionalFilm.isPresent()) {

                optionalFilm.get().setOpinion(opinion);
                filmRepository.save(filmRepository.findById(id).get());
                return ResponseEntity.status(201).body("Opinion adderad!");

            } else {

                return ResponseEntity.status(404).body("kan int finne film");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("något fel");
        }
    }

    @Override
    public ResponseEntity<Response> getFilmWithAdditionalInfo(int filmId, boolean opinion, boolean description) {

        FilmModel film;
        FilmDTO filmDTO = new FilmDTO();
        try {

            if (filmRepository.findById(filmId).isPresent()) {
                film = filmRepository.findById(filmId).get();
            } else {
                return ResponseEntity.status(404).body(new ErrorResponse("Film finns inte"));
            }

            if (opinion == true && description == true) {
                filmDTO.setDescription(film.getOverview());
                filmDTO.setOpinion(film.getOpinion());
                filmDTO.setTitle(film.getOriginal_title());

                return ResponseEntity.ok(filmDTO);
            }

            if (opinion == true) {
                filmDTO.setTitle(film.getOriginal_title());
                filmDTO.setOpinion(film.getOpinion());
                filmDTO.setDescription("inget här");

                return ResponseEntity.ok(filmDTO);

            }

            if (description == true) {
                filmDTO.setTitle(film.getOriginal_title());
                filmDTO.setDescription(film.getOverview());
                filmDTO.setOpinion("inget här");

                return ResponseEntity.ok(filmDTO);
            }
            filmDTO.setTitle(film.getOriginal_title());
            filmDTO.setDescription("inget här");
            filmDTO.setOpinion("inget här");

            return ResponseEntity.ok(filmDTO);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel på databas"));
        }

    }

    @Override
    public ResponseEntity<Response> getInfo() {

        int USfilms = 0;
        int nonUSfilms = 0;

        ArrayList<FilmModel> adultFilms = new ArrayList<>();
        ArrayList<String> budgetFilms = new ArrayList<>();

        try {


            List<FilmModel> films = findAll();
            Collections.sort(films, new Comparator<FilmModel>() {
                @Override
                public int compare(FilmModel o1, FilmModel o2) {
                    return Integer.compare(o1.getBudget(), o2.getBudget());
                }
            });

            for (FilmModel film : films) {

                if (film.isAdult() == true) {
                    adultFilms.add(film);
                }

                if (Objects.equals(film.getOrigin_country().get(0), "US")) {
                    USfilms++;
                } else {
                    nonUSfilms++;
                }

                System.out.println(film.getOriginal_title() + ": " + film.getBudget() + " origin country " + film.getOrigin_country().get(0));
                budgetFilms.add(film.getOriginal_title() + " " + film.getBudget());
            }


            if (findAll().isEmpty()) {
                return ResponseEntity.ok(new ErrorResponse("Du har inga sparade filmer"));
            }


            IntegerResponse intRes = (IntegerResponse) getAverageRuntime().getBody();
            int averageRuntime = intRes.getAverageRuntime();

            return ResponseEntity.ok(new ErrorResponse("du har: " + findAll().size() + " filmer sparade." + "\n\r" +
                    " medellängden på filmerna är: " + averageRuntime + " minuter, " +
                    "varav " + adultFilms.size() + " porrfilm(er)" + "budge rank " + budgetFilms + " av dessa är " + USfilms + " amerkikanska och resten " + nonUSfilms + " från andra länder"));


        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }





//    @Override
//    public ResponseEntity<Response> getInfo() {
//        try {
//            List<FilmModel> films = findAll();
//
//            if (films.isEmpty()) {
//                return ResponseEntity.ok(new ErrorResponse("Du har inga sparade filmer"));
//            }
//
//            // Count and categorize films
//            long usFilmCount = films.stream()
//                    .filter(film -> "US".equals(film.getOrigin_country().get(0)))
//                    .count();
//
//            long nonUsFilmCount = films.size() - usFilmCount;
//
//            List<FilmModel> adultFilms = films.stream()
//                    .filter(FilmModel::isAdult)
//                    .toList();
//
//            List<String> budgetFilms = films.stream()
//                    .sorted(Comparator.comparingInt(FilmModel::getBudget))
//                    .map(film -> film.getOriginal_title() + " " + film.getBudget())
//                    .toList();
//
//            IntegerResponse intRes = (IntegerResponse) getAverageRuntime().getBody();
//            int averageRuntime = intRes != null ? intRes.getAverageRuntime() : 0;
//
//            String responseMessage = String.format("Du har: %d filmer sparade.\n" +
//                            "Medellängden på filmerna är: %d minuter, " +
//                            "varav %d porrfilm(er). " +
//                            "Budget rank: %s. " +
//                            "Av dessa är %d amerikanska och resten %d från andra länder.",
//                    films.size(), averageRuntime, adultFilms.size(), budgetFilms, usFilmCount, nonUsFilmCount);
//
//            return ResponseEntity.ok(new ErrorResponse(responseMessage));
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Något fel inträffade"));
//        }
//    }

}
