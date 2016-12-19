package model;

import java.io.Serializable;

public class MovieSet implements Serializable {

    int id;
    String imdbID;
    String title;
    int year;
    String rating;
    int runtime;
    String genre;
    String released;
    String director;
    String writer;
    String cast;
    String metacritic;
    float imdbRating;
    int imdbVotes;
    String poster;
    String plot;
    String fullPlot;
    String language;
    String country;
    String awards;
    String lastUpdated;
    boolean isInIgnoreList = false;

    public MovieSet() {
    }

    public MovieSet(int id, String imdbID, String title, int year, String rating, int runtime, String genre, String released, String director, String writer, String cast, String metacritic,
                    float imdbRating, int imdbVotes, String poster, String plot, String fullPlot, String language, String country, String awards, String lastUpdated) {
        super();
        this.id = id;
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.runtime = runtime;
        this.genre = genre;
        this.released = released;
        this.director = director;
        this.writer = writer;
        this.cast = cast;
        this.metacritic = metacritic;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.poster = poster;
        this.plot = plot;
        this.fullPlot = fullPlot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImdbID() {
        return imdbID;
    }
    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public int getRuntime() {
        return runtime;
    }
    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getReleased() {
        return released;
    }
    public void setReleased(String released) {
        this.released = released;
    }
    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }
    public String getWriter() {
        return writer;
    }
    public void setWriter(String writer) {
        this.writer = writer;
    }
    public String getCast() {
        return cast;
    }
    public void setCast(String cast) {
        this.cast = cast;
    }
    public String getMetacritic() {
        return metacritic;
    }
    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }
    public float getImdbRating() {
        return imdbRating;
    }
    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
    }
    public int getImdbVotes() {
        return imdbVotes;
    }
    public void setImdbVotes(int imdbVotes) {
        this.imdbVotes = imdbVotes;
    }
    public String getPoster() {
        return poster;
    }
    public void setPoster(String poster) {
        this.poster = poster;
    }
    public String getPlot() {
        return plot;
    }
    public void setPlot(String plot) {
        this.plot = plot;
    }
    public String getFullPlot() {
        return fullPlot;
    }
    public void setFullPlot(String fullPlot) {
        this.fullPlot = fullPlot;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getAwards() {
        return awards;
    }
    public void setAwards(String awards) {
        this.awards = awards;
    }
    public String getLastUpdated() {
        return lastUpdated;
    }
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public boolean isInIgnoreList() {
        return isInIgnoreList;
    }
    public void setIsInIgnoreList(boolean isInIgnoreList) {
        this.isInIgnoreList = isInIgnoreList;
    }

}
