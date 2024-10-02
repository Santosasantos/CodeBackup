package com.bracits.epms.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A FeedbackSubType.
 */
@Entity
@Table(name = "feedback_sub_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackSubType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "feedbacksubname", nullable = false)
    private String feedbacksubname;

    @NotNull
    @Column(name = "feedbackdescription", nullable = false)
    private String feedbackdescription;

    @ManyToOne(fetch = FetchType.LAZY)
    private FeedbackType feedbackType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FeedbackSubType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedbacksubname() {
        return this.feedbacksubname;
    }

    public FeedbackSubType feedbacksubname(String feedbacksubname) {
        this.setFeedbacksubname(feedbacksubname);
        return this;
    }

    public void setFeedbacksubname(String feedbacksubname) {
        this.feedbacksubname = feedbacksubname;
    }

    public String getFeedbackdescription() {
        return this.feedbackdescription;
    }

    public FeedbackSubType feedbackdescription(String feedbackdescription) {
        this.setFeedbackdescription(feedbackdescription);
        return this;
    }

    public void setFeedbackdescription(String feedbackdescription) {
        this.feedbackdescription = feedbackdescription;
    }

    public FeedbackType getFeedbackType() {
        return this.feedbackType;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public FeedbackSubType feedbackType(FeedbackType feedbackType) {
        this.setFeedbackType(feedbackType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackSubType)) {
            return false;
        }
        return getId() != null && getId().equals(((FeedbackSubType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackSubType{" +
            "id=" + getId() +
            ", feedbacksubname='" + getFeedbacksubname() + "'" +
            ", feedbackdescription='" + getFeedbackdescription() + "'" +
            "}";
    }
}
