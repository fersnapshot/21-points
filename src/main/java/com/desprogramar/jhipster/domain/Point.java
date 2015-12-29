package com.desprogramar.jhipster.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Point.
 */
@Entity
@Table(name = "point")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "point")
public class Point implements Serializable {

	private static final long serialVersionUID = 5632380433273990085L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "exercise")
    private Boolean exercise;

    @Column(name = "meals")
    private Boolean meals;

    @Column(name = "alcohol")
    private Boolean alcohol;

    @Size(max = 140)
    @Column(name = "notes", length = 140)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Point() {
        super();
    }
    public Point(LocalDate date, Boolean exercise, Boolean meals, Boolean alcohol, User user) {
        this();
        this.date = date;
        this.exercise = exercise;
        this.meals = meals;
        this.alcohol = alcohol;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

//    public void setVersion(Integer version) {
//        this.version = version;
//    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getExercise() {
        return exercise == null ? new Boolean(false) : exercise;
    }

    public void setExercise(Boolean exercise) {
        this.exercise = exercise;
    }

    public Boolean getMeals() {
        return meals == null ? new Boolean(false) : meals;
    }

    public void setMeals(Boolean meals) {
        this.meals = meals;
    }

    public Boolean getAlcohol() {
        return alcohol == null ? new Boolean(false) : alcohol;
    }

    public void setAlcohol(Boolean alcohol) {
        this.alcohol = alcohol;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Objects.equals(id, point.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Point{" +
            "id=" + id +
            ", version='" + version + "'" +
            ", date='" + date + "'" +
            ", exercise='" + exercise + "'" +
            ", meals='" + meals + "'" +
            ", alcohol='" + alcohol + "'" +
            ", notes='" + notes + "'" +
            '}';
    }
}
