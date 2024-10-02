package com.bracits.epms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A ExtraquestionAns.
 */
@Entity
@Table(name = "extraquestion_ans")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ExtraquestionAns implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "questionans", nullable = false)
    private String questionans;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "questionAns", "feedback" }, allowSetters = true)
    private Extraquestion questions;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ExtraquestionAns id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionans() {
        return this.questionans;
    }

    public ExtraquestionAns questionans(String questionans) {
        this.setQuestionans(questionans);
        return this;
    }

    public void setQuestionans(String questionans) {
        this.questionans = questionans;
    }

    public Extraquestion getQuestions() {
        return this.questions;
    }

    public void setQuestions(Extraquestion extraquestion) {
        this.questions = extraquestion;
    }

    public ExtraquestionAns questions(Extraquestion extraquestion) {
        this.setQuestions(extraquestion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ExtraquestionAns)) {
            return false;
        }
        return getId() != null && getId().equals(((ExtraquestionAns) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ExtraquestionAns{" +
            "id=" + getId() +
            ", questionans='" + getQuestionans() + "'" +
            "}";
    }
}
