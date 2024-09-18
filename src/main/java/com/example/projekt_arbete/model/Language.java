package com.example.projekt_arbete.model;

import jakarta.persistence.*;

//@Entity
@Embeddable
public class Language {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String englishName;

    private String iso639_1;

    private String name;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getIso639_1() {
        return iso639_1;
    }

    public void setIso639_1(String iso639_1) {
        this.iso639_1 = iso639_1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

