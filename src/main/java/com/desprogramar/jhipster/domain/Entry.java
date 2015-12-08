package com.desprogramar.jhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Entry.
 */
@Entity
@Table(name = "entry")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "entry")
public class Entry implements Serializable {

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
    private Integer exercise;

    @Column(name = "meals")
    private Integer meals;

    @Column(name = "alcohol")
    private Integer alcohol;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @OneToMany(mappedBy = "entry")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Metrica> metricas = new HashSet<>();


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

    public Integer getExercise() {
        return exercise;
    }

    public void setExercise(Integer exercise) {
        this.exercise = exercise;
    }

    public Integer getMeals() {
        return meals;
    }

    public void setMeals(Integer meals) {
        this.meals = meals;
    }

    public Integer getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(Integer alcohol) {
        this.alcohol = alcohol;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Set<Metrica> getMetricas() {
        return metricas;
    }

    public void setMetricas(Set<Metrica> metricas) {
        this.metricas = metricas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entry entry = (Entry) o;
        return Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Entry{" +
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
