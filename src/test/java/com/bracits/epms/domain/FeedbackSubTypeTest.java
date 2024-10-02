package com.bracits.epms.domain;

import static com.bracits.epms.domain.FeedbackSubTypeTestSamples.*;
import static com.bracits.epms.domain.FeedbackTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackSubTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackSubType.class);
        FeedbackSubType feedbackSubType1 = getFeedbackSubTypeSample1();
        FeedbackSubType feedbackSubType2 = new FeedbackSubType();
        assertThat(feedbackSubType1).isNotEqualTo(feedbackSubType2);

        feedbackSubType2.setId(feedbackSubType1.getId());
        assertThat(feedbackSubType1).isEqualTo(feedbackSubType2);

        feedbackSubType2 = getFeedbackSubTypeSample2();
        assertThat(feedbackSubType1).isNotEqualTo(feedbackSubType2);
    }

    @Test
    void feedbackTypeTest() {
        FeedbackSubType feedbackSubType = getFeedbackSubTypeRandomSampleGenerator();
        FeedbackType feedbackTypeBack = getFeedbackTypeRandomSampleGenerator();

        feedbackSubType.setFeedbackType(feedbackTypeBack);
        assertThat(feedbackSubType.getFeedbackType()).isEqualTo(feedbackTypeBack);

        feedbackSubType.feedbackType(null);
        assertThat(feedbackSubType.getFeedbackType()).isNull();
    }
}
