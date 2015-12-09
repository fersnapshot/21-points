package com.desprogramar.jhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.desprogramar.jhipster.domain.enumeration.Units;

/**
 * A Preference.
 */
@Entity
@Table(name = "preference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "preference")
public class Preference implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @NotNull
    @Min(value = 10)
    @Max(value = 21)
    @Column(name = "weekly_goal", nullable = false)
    private Integer weekly_goal;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "weight_units", nullable = false)
    private Units weight_units;

    @OneToOne
    @JsonIgnore
    private User user;


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

    public Integer getWeekly_goal() {
        return weekly_goal;
    }

    public void setWeekly_goal(Integer weekly_goal) {
        this.weekly_goal = weekly_goal;
    }

    public Units getWeight_units() {
        return weight_units;
    }

    public void setWeight_units(Units weight_units) {
        this.weight_units = weight_units;
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
        Preference preference = (Preference) o;
        return Objects.equals(id, preference.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Preference{" +
            "id=" + id +
            ", version='" + version + "'" +
            ", weekly_goal='" + weekly_goal + "'" +
            ", weight_units='" + weight_units + "'" +
            '}';
    }
}
