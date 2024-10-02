package com.bracits.epms.domain;

import com.bracits.epms.domain.enumeration.RecommendationValue;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A TeachOther.
 */
@Entity
@Table(name = "teach_other")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TeachOther implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "technical_skill", nullable = false)
    private String technicalSkill;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation", nullable = false)
    private RecommendationValue recommendation;

    @Column(name = "particular_strengh")
    private String particularStrengh;

    @Column(name = "whynot_recommend")
    private String whynotRecommend;

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

    public TeachOther id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTechnicalSkill() {
        return this.technicalSkill;
    }

    public TeachOther technicalSkill(String technicalSkill) {
        this.setTechnicalSkill(technicalSkill);
        return this;
    }

    public void setTechnicalSkill(String technicalSkill) {
        this.technicalSkill = technicalSkill;
    }

    public RecommendationValue getRecommendation() {
        return this.recommendation;
    }

    public TeachOther recommendation(RecommendationValue recommendation) {
        this.setRecommendation(recommendation);
        return this;
    }

    public void setRecommendation(RecommendationValue recommendation) {
        this.recommendation = recommendation;
    }

    public String getParticularStrengh() {
        return this.particularStrengh;
    }

    public TeachOther particularStrengh(String particularStrengh) {
        this.setParticularStrengh(particularStrengh);
        return this;
    }

    public void setParticularStrengh(String particularStrengh) {
        this.particularStrengh = particularStrengh;
    }

    public String getWhynotRecommend() {
        return this.whynotRecommend;
    }

    public TeachOther whynotRecommend(String whynotRecommend) {
        this.setWhynotRecommend(whynotRecommend);
        return this;
    }

    public void setWhynotRecommend(String whynotRecommend) {
        this.whynotRecommend = whynotRecommend;
    }

    public FeedbackResponder getResponder() {
        return this.responder;
    }

    public void setResponder(FeedbackResponder feedbackResponder) {
        this.responder = feedbackResponder;
    }

    public TeachOther responder(FeedbackResponder feedbackResponder) {
        this.setResponder(feedbackResponder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeachOther)) {
            return false;
        }
        return getId() != null && getId().equals(((TeachOther) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeachOther{" +
            "id=" + getId() +
            ", technicalSkill='" + getTechnicalSkill() + "'" +
            ", recommendation='" + getRecommendation() + "'" +
            ", particularStrengh='" + getParticularStrengh() + "'" +
            ", whynotRecommend='" + getWhynotRecommend() + "'" +
            "}";
    }
}
