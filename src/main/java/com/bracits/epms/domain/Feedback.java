package com.bracits.epms.domain;

import com.bracits.epms.domain.enumeration.FeedbackStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Feedback.
 */
@Entity
@Table(name = "feedback")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "request_date", nullable = false)
    private Instant requestDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FeedbackStatus status;

    @Column(name = "response_date")
    private LocalDate responseDate;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "assessment_year", nullable = false)
    private Integer assessmentYear;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
    @JsonIgnoreProperties(
        value = { "feedbackDetails", "teachOthers", "skillDevelopmentDetails", "employee", "feedback" },
        allowSetters = true
    )
    private Set<FeedbackResponder> responders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "feedback")
    @JsonIgnoreProperties(value = { "questionAns", "feedback" }, allowSetters = true)
    private Set<Extraquestion> extraQuestions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "supervisor" }, allowSetters = true)
    private Employee requester;

    @ManyToOne(fetch = FetchType.LAZY)
    private RatingScale ratingScale;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feedback id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getRequestDate() {
        return this.requestDate;
    }

    public Feedback requestDate(Instant requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public FeedbackStatus getStatus() {
        return this.status;
    }

    public Feedback status(FeedbackStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(FeedbackStatus status) {
        this.status = status;
    }

    public LocalDate getResponseDate() {
        return this.responseDate;
    }

    public Feedback responseDate(LocalDate responseDate) {
        this.setResponseDate(responseDate);
        return this;
    }

    public void setResponseDate(LocalDate responseDate) {
        this.responseDate = responseDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public Feedback createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getAssessmentYear() {
        return this.assessmentYear;
    }

    public Feedback assessmentYear(Integer assessmentYear) {
        this.setAssessmentYear(assessmentYear);
        return this;
    }

    public void setAssessmentYear(Integer assessmentYear) {
        this.assessmentYear = assessmentYear;
    }

    public Set<FeedbackResponder> getResponders() {
        return this.responders;
    }

    public void setResponders(Set<FeedbackResponder> feedbackResponders) {
        if (this.responders != null) {
            this.responders.forEach(i -> i.setFeedback(null));
        }
        if (feedbackResponders != null) {
            feedbackResponders.forEach(i -> i.setFeedback(this));
        }
        this.responders = feedbackResponders;
    }

    public Feedback responders(Set<FeedbackResponder> feedbackResponders) {
        this.setResponders(feedbackResponders);
        return this;
    }

    public Feedback addResponders(FeedbackResponder feedbackResponder) {
        this.responders.add(feedbackResponder);
        feedbackResponder.setFeedback(this);
        return this;
    }

    public Feedback removeResponders(FeedbackResponder feedbackResponder) {
        this.responders.remove(feedbackResponder);
        feedbackResponder.setFeedback(null);
        return this;
    }

    public Set<Extraquestion> getExtraQuestions() {
        return this.extraQuestions;
    }

    public void setExtraQuestions(Set<Extraquestion> extraquestions) {
        if (this.extraQuestions != null) {
            this.extraQuestions.forEach(i -> i.setFeedback(null));
        }
        if (extraquestions != null) {
            extraquestions.forEach(i -> i.setFeedback(this));
        }
        this.extraQuestions = extraquestions;
    }

    public Feedback extraQuestions(Set<Extraquestion> extraquestions) {
        this.setExtraQuestions(extraquestions);
        return this;
    }

    public Feedback addExtraQuestions(Extraquestion extraquestion) {
        this.extraQuestions.add(extraquestion);
        extraquestion.setFeedback(this);
        return this;
    }

    public Feedback removeExtraQuestions(Extraquestion extraquestion) {
        this.extraQuestions.remove(extraquestion);
        extraquestion.setFeedback(null);
        return this;
    }

    public Employee getRequester() {
        return this.requester;
    }

    public void setRequester(Employee employee) {
        this.requester = employee;
    }

    public Feedback requester(Employee employee) {
        this.setRequester(employee);
        return this;
    }

    public RatingScale getRatingScale() {
        return this.ratingScale;
    }

    public void setRatingScale(RatingScale ratingScale) {
        this.ratingScale = ratingScale;
    }

    public Feedback ratingScale(RatingScale ratingScale) {
        this.setRatingScale(ratingScale);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feedback)) {
            return false;
        }
        return getId() != null && getId().equals(((Feedback) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feedback{" +
            "id=" + getId() +
            ", requestDate='" + getRequestDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", responseDate='" + getResponseDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", assessmentYear=" + getAssessmentYear() +
            "}";
    }
}
