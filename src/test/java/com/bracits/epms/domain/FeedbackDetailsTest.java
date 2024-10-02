package com.bracits.epms.domain;

import static com.bracits.epms.domain.FeedbackDetailsTestSamples.*;
import static com.bracits.epms.domain.FeedbackResponderTestSamples.*;
import static com.bracits.epms.domain.FeedbackSubTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackDetails.class);
        FeedbackDetails feedbackDetails1 = getFeedbackDetailsSample1();
        FeedbackDetails feedbackDetails2 = new FeedbackDetails();
        assertThat(feedbackDetails1).isNotEqualTo(feedbackDetails2);

        feedbackDetails2.setId(feedbackDetails1.getId());
        assertThat(feedbackDetails1).isEqualTo(feedbackDetails2);

        feedbackDetails2 = getFeedbackDetailsSample2();
        assertThat(feedbackDetails1).isNotEqualTo(feedbackDetails2);
    }

    @Test
    void feedbackSubTypeTest() {
        FeedbackDetails feedbackDetails = getFeedbackDetailsRandomSampleGenerator();
        FeedbackSubType feedbackSubTypeBack = getFeedbackSubTypeRandomSampleGenerator();

        feedbackDetails.setFeedbackSubType(feedbackSubTypeBack);
        assertThat(feedbackDetails.getFeedbackSubType()).isEqualTo(feedbackSubTypeBack);

        feedbackDetails.feedbackSubType(null);
        assertThat(feedbackDetails.getFeedbackSubType()).isNull();
    }

    @Test
    void responderTest() {
        FeedbackDetails feedbackDetails = getFeedbackDetailsRandomSampleGenerator();
        FeedbackResponder feedbackResponderBack = getFeedbackResponderRandomSampleGenerator();

        feedbackDetails.setResponder(feedbackResponderBack);
        assertThat(feedbackDetails.getResponder()).isEqualTo(feedbackResponderBack);

        feedbackDetails.responder(null);
        assertThat(feedbackDetails.getResponder()).isNull();
    }
}
