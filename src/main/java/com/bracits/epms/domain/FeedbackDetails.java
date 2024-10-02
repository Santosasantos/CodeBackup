package com.bracits.epms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A FeedbackDetails.
 */
@Entity
@Table(name = "feedback_details")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeedbackDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "commentsforfeedbacksubtype")
    private String commentsforfeedbacksubtype;

    @Column(name = "ratingvalue")
    private Integer ratingvalue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "feedbackType" }, allowSetters = true)
    private FeedbackSubType feedbackSubType;

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

    public FeedbackDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentsforfeedbacksubtype() {
        return this.commentsforfeedbacksubtype;
    }

    public FeedbackDetails commentsforfeedbacksubtype(String commentsforfeedbacksubtype) {
        this.setCommentsforfeedbacksubtype(commentsforfeedbacksubtype);
        return this;
    }

    public void setCommentsforfeedbacksubtype(String commentsforfeedbacksubtype) {
        this.commentsforfeedbacksubtype = commentsforfeedbacksubtype;
    }

    public Integer getRatingvalue() {
        return this.ratingvalue;
    }

    public FeedbackDetails ratingvalue(Integer ratingvalue) {
        this.setRatingvalue(ratingvalue);
        return this;
    }

    public void setRatingvalue(Integer ratingvalue) {
        this.ratingvalue = ratingvalue;
    }

    public FeedbackSubType getFeedbackSubType() {
        return this.feedbackSubType;
    }

    public void setFeedbackSubType(FeedbackSubType feedbackSubType) {
        this.feedbackSubType = feedbackSubType;
    }

    public FeedbackDetails feedbackSubType(FeedbackSubType feedbackSubType) {
        this.setFeedbackSubType(feedbackSubType);
        return this;
    }

    public FeedbackResponder getResponder() {
        return this.responder;
    }

    public void setResponder(FeedbackResponder feedbackResponder) {
        this.responder = feedbackResponder;
    }

    public FeedbackDetails responder(FeedbackResponder feedbackResponder) {
        this.setResponder(feedbackResponder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FeedbackDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((FeedbackDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeedbackDetails{" +
            "id=" + getId() +
            ", commentsforfeedbacksubtype='" + getCommentsforfeedbacksubtype() + "'" +
            ", ratingvalue=" + getRatingvalue() +
            "}";
    }
}
