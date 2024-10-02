package com.bracits.epms.domain;

import com.bracits.epms.domain.enumeration.FeedbackStatus;
import com.bracits.epms.domain.enumeration.ResponderCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A FeedbackResponder.
 */
@Entity
@Table(name = "feedback_responder")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackResponder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ResponderCategory category;

    @Column(name = "stakeholder_email")
    private String stakeholderEmail;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "responder_status", nullable = false)
    private FeedbackStatus responderStatus;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responder")
    @JsonIgnoreProperties(value = { "feedbackSubType", "responder" }, allowSetters = true)
    private Set<FeedbackDetails> feedbackDetails = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responder")
    @JsonIgnoreProperties(value = { "responder" }, allowSetters = true)
    private Set<TeachOther> teachOthers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "responder")
    @JsonIgnoreProperties(value = { "skillDevelopmentType", "responder" }, allowSetters = true)
    private Set<SkillDevelopmentDetails> skillDevelopmentDetails = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "supervisor" }, allowSetters = true)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "responders", "extraQuestions", "requester", "ratingScale" }, allowSetters = true)
    private Feedback feedback;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackResponder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResponderCategory getCategory() {
        return this.category;
    }

    public FeedbackResponder category(ResponderCategory category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(ResponderCategory category) {
        this.category = category;
    }

    public String getStakeholderEmail() {
        return this.stakeholderEmail;
    }

    public FeedbackResponder stakeholderEmail(String stakeholderEmail) {
        this.setStakeholderEmail(stakeholderEmail);
        return this;
    }

    public void setStakeholderEmail(String stakeholderEmail) {
        this.stakeholderEmail = stakeholderEmail;
    }

    public FeedbackStatus getResponderStatus() {
        return this.responderStatus;
    }

    public FeedbackResponder responderStatus(FeedbackStatus responderStatus) {
        this.setResponderStatus(responderStatus);
        return this;
    }

    public void setResponderStatus(FeedbackStatus responderStatus) {
        this.responderStatus = responderStatus;
    }

    public Set<FeedbackDetails> getFeedbackDetails() {
        return this.feedbackDetails;
    }

    public void setFeedbackDetails(Set<FeedbackDetails> feedbackDetails) {
        if (this.feedbackDetails != null) {
            this.feedbackDetails.forEach(i -> i.setResponder(null));
        }
        if (feedbackDetails != null) {
            feedbackDetails.forEach(i -> i.setResponder(this));
        }
        this.feedbackDetails = feedbackDetails;
    }

    public FeedbackResponder feedbackDetails(Set<FeedbackDetails> feedbackDetails) {
        this.setFeedbackDetails(feedbackDetails);
        return this;
    }

    public FeedbackResponder addFeedbackDetails(FeedbackDetails feedbackDetails) {
        this.feedbackDetails.add(feedbackDetails);
        feedbackDetails.setResponder(this);
        return this;
    }

    public FeedbackResponder removeFeedbackDetails(FeedbackDetails feedbackDetails) {
        this.feedbackDetails.remove(feedbackDetails);
        feedbackDetails.setResponder(null);
        return this;
    }

    public Set<TeachOther> getTeachOthers() {
        return this.teachOthers;
    }

    public void setTeachOthers(Set<TeachOther> teachOthers) {
        if (this.teachOthers != null) {
            this.teachOthers.forEach(i -> i.setResponder(null));
        }
        if (teachOthers != null) {
            teachOthers.forEach(i -> i.setResponder(this));
        }
        this.teachOthers = teachOthers;
    }

    public FeedbackResponder teachOthers(Set<TeachOther> teachOthers) {
        this.setTeachOthers(teachOthers);
        return this;
    }

    public FeedbackResponder addTeachOthers(TeachOther teachOther) {
        this.teachOthers.add(teachOther);
        teachOther.setResponder(this);
        return this;
    }

    public FeedbackResponder removeTeachOthers(TeachOther teachOther) {
        this.teachOthers.remove(teachOther);
        teachOther.setResponder(null);
        return this;
    }

    public Set<SkillDevelopmentDetails> getSkillDevelopmentDetails() {
        return this.skillDevelopmentDetails;
    }

    public void setSkillDevelopmentDetails(Set<SkillDevelopmentDetails> skillDevelopmentDetails) {
        if (this.skillDevelopmentDetails != null) {
            this.skillDevelopmentDetails.forEach(i -> i.setResponder(null));
        }
        if (skillDevelopmentDetails != null) {
            skillDevelopmentDetails.forEach(i -> i.setResponder(this));
        }
        this.skillDevelopmentDetails = skillDevelopmentDetails;
    }

    public FeedbackResponder skillDevelopmentDetails(Set<SkillDevelopmentDetails> skillDevelopmentDetails) {
        this.setSkillDevelopmentDetails(skillDevelopmentDetails);
        return this;
    }

    public FeedbackResponder addSkillDevelopmentDetails(SkillDevelopmentDetails skillDevelopmentDetails) {
        this.skillDevelopmentDetails.add(skillDevelopmentDetails);
        skillDevelopmentDetails.setResponder(this);
        return this;
    }

    public FeedbackResponder removeSkillDevelopmentDetails(SkillDevelopmentDetails skillDevelopmentDetails) {
        this.skillDevelopmentDetails.remove(skillDevelopmentDetails);
        skillDevelopmentDetails.setResponder(null);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public FeedbackResponder employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Feedback getFeedback() {
        return this.feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public FeedbackResponder feedback(Feedback feedback) {
        this.setFeedback(feedback);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackResponder)) {
            return false;
        }
        return getId() != null && getId().equals(((FeedbackResponder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackResponder{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", stakeholderEmail='" + getStakeholderEmail() + "'" +
            ", responderStatus='" + getResponderStatus() + "'" +
            "}";
    }
}
