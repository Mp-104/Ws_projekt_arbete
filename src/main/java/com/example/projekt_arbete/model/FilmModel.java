package com.example.projekt_arbete.model;

import com.example.projekt_arbete.response.Response;
import jakarta.persistence.*;
import com.example.projekt_arbete.model.Genre;

import java.util.List;

@Entity
public class FilmModel implements Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filmid;

    private boolean adult;
    private String backdropPath;
    private String belongsToCollection;
    private int budget;

    @ElementCollection
    private List<Genre> genres;

    private String homepage;
    private int id;
    private String imdbId;
    private String originalLanguage;
    private String original_title;
    private List<String> origin_country;
    @Column(length = 1000)
    private String overview;
    private double popularity;
    private String posterPath;


    @ElementCollection
    private List<ProductionCompany> productionCompanies;

    @ElementCollection
    private List<String> productionCountries;
    private String releaseDate;
    private long revenue;
    private int runtime;
    @ElementCollection
    private List<Language> spokenLanguages;
    private String status;
    private String tagline;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    @Column(length = 1000)
    private String opinion;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(String belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public List<Language> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<Language> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public List<String> getOrigin_country() {
        return origin_country;
    }

    public void setOrigin_country(List<String> origin_country) {
        this.origin_country = origin_country;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getOriginal_title () {
        return original_title;
    }

    public void setOriginal_title (String original_title) {
        this.original_title = original_title;
    }

}
