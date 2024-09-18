package com.example.projekt_arbete.model;

import jakarta.persistence.*;

//@Entity
@Embeddable
public class ProductionCompany {

    //@Id
    private Long id;

    private String name;

    private String originCountry;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }
}
