package com.example.projekt_arbete.response;

import com.example.projekt_arbete.model.FilmModel;

import java.util.List;

public class ListResponse implements Response{
    private List<FilmModel> filmList;

    public ListResponse () {

    }

    public ListResponse (List<FilmModel> filmList) {
        this.filmList = filmList;
    }

    public List<FilmModel> getFilmList () {
        return filmList;
    }

    public void setFilmList (List<FilmModel> filmList) {
        this.filmList = filmList;
    }
}
