package com.bracits.epms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A RatingScale.
 */
@Entity
@Table(name = "rating_scale")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RatingScale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "scaletype", nullable = false)
    private String scaletype;

    @NotNull
    @Column(name = "ratingscales", nullable = false)
    private String ratingscales;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RatingScale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getScaletype() {
        return this.scaletype;
    }

    public RatingScale scaletype(String scaletype) {
        this.setScaletype(scaletype);
        return this;
    }

    public void setScaletype(String scaletype) {
        this.scaletype = scaletype;
    }

    public String getRatingscales() {
        return this.ratingscales;
    }

    public RatingScale ratingscales(String ratingscales) {
        this.setRatingscales(ratingscales);
        return this;
    }

    public void setRatingscales(String ratingscales) {
        this.ratingscales = ratingscales;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RatingScale)) {
            return false;
        }
        return getId() != null && getId().equals(((RatingScale) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RatingScale{" +
            "id=" + getId() +
            ", scaletype='" + getScaletype() + "'" +
            ", ratingscales='" + getRatingscales() + "'" +
            "}";
    }
}
