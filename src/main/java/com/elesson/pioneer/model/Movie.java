package com.elesson.pioneer.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Represents a row in the &quot;movies&quot; database table,
 * with each column mapped to a property of this class.
 */
public class Movie extends Entity {
    private String name;
    private String genre;
    private Integer duration;
    private Integer year;
    private Boolean active;

    /**
     * The default constructor.
     */
    public Movie() {
    }

    /**
     * Instantiates a new Movie.
     *
     * @param id       the id
     */
    public Movie(Integer id) {
        super(id);
    }

    /**
     * Instantiates a new Movie.
     *
     * @param id       the id
     * @param name     the name
     * @param genre    the genre
     * @param duration the duration
     * @param year     the year
     * @param active   the active
     */
    public Movie(Integer id, String name, String genre, Integer duration, Integer year, Boolean active) {
        super(id);
        this.name = name;
        this.genre = genre;
        this.duration = duration;
        this.year = year;
        this.active = active;
    }

    /**
     * Instantiates a new Movie.
     *
     * @param name     the name
     * @param genre    the genre
     * @param duration the duration
     * @param year     the year
     * @param active   the active
     */
    public Movie(String name, String genre, Integer duration, Integer year, Boolean active) {
        this(null, name, genre, duration, year, active);
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets genre.
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Gets year.
     *
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Is active boolean.
     *
     * @return true if it is available for further manipulation in other services
     */
    public Boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(name, movie.name) &&
                Objects.equals(genre, movie.genre) &&
                Objects.equals(duration, movie.duration) &&
                Objects.equals(year, movie.year) &&
                Objects.equals(active, movie.active);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, genre, duration, year, active);
    }
}
