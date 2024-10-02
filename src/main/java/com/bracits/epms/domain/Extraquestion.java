package com.bracits.epms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Extraquestion.
 */
@Entity
@Table(name = "extraquestion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Extraquestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "question", nullable = false)
    private String question;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "questions")
    @JsonIgnoreProperties(value = { "questions" }, allowSetters = true)
    private Set<ExtraquestionAns> questionAns = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responders", "extraQuestions", "requester", "ratingScale" }, allowSetters = true)
    private Feedback feedback;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Extraquestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public Extraquestion question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Set<ExtraquestionAns> getQuestionAns() {
        return this.questionAns;
    }

    public void setQuestionAns(Set<ExtraquestionAns> extraquestionAns) {
        if (this.questionAns != null) {
            this.questionAns.forEach(i -> i.setQuestions(null));
        }
        if (extraquestionAns != null) {
            extraquestionAns.forEach(i -> i.setQuestions(this));
        }
        this.questionAns = extraquestionAns;
    }

    public Extraquestion questionAns(Set<ExtraquestionAns> extraquestionAns) {
        this.setQuestionAns(extraquestionAns);
        return this;
    }

    public Extraquestion addQuestionAns(ExtraquestionAns extraquestionAns) {
        this.questionAns.add(extraquestionAns);
        extraquestionAns.setQuestions(this);
        return this;
    }

    public Extraquestion removeQuestionAns(ExtraquestionAns extraquestionAns) {
        this.questionAns.remove(extraquestionAns);
        extraquestionAns.setQuestions(null);
        return this;
    }

    public Feedback getFeedback() {
        return this.feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Extraquestion feedback(Feedback feedback) {
        this.setFeedback(feedback);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Extraquestion)) {
            return false;
        }
        return getId() != null && getId().equals(((Extraquestion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Extraquestion{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            "}";
    }
}
