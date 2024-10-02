package com.bracits.epms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A SkillDevelopmentDetails.
 */
@Entity
@Table(name = "skill_development_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SkillDevelopmentDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private SkillDevelopmentType skillDevelopmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "feedbackDetails", "teachOthers", "skillDevelopmentDetails", "employee", "feedback" },
        allowSetters = true
    )
    private FeedbackResponder responder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SkillDevelopmentDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkillDevelopmentType getSkillDevelopmentType() {
        return this.skillDevelopmentType;
    }

    public void setSkillDevelopmentType(SkillDevelopmentType skillDevelopmentType) {
        this.skillDevelopmentType = skillDevelopmentType;
    }

    public SkillDevelopmentDetails skillDevelopmentType(SkillDevelopmentType skillDevelopmentType) {
        this.setSkillDevelopmentType(skillDevelopmentType);
        return this;
    }

    public FeedbackResponder getResponder() {
        return this.responder;
    }

    public void setResponder(FeedbackResponder feedbackResponder) {
        this.responder = feedbackResponder;
    }

    public SkillDevelopmentDetails responder(FeedbackResponder feedbackResponder) {
        this.setResponder(feedbackResponder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SkillDevelopmentDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((SkillDevelopmentDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SkillDevelopmentDetails{" +
            "id=" + getId() +
            "}";
    }
}
