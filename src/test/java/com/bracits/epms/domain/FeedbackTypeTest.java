package com.bracits.epms.domain;

import static com.bracits.epms.domain.FeedbackTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackType.class);
        FeedbackType feedbackType1 = getFeedbackTypeSample1();
        FeedbackType feedbackType2 = new FeedbackType();
        assertThat(feedbackType1).isNotEqualTo(feedbackType2);

        feedbackType2.setId(feedbackType1.getId());
        assertThat(feedbackType1).isEqualTo(feedbackType2);

        feedbackType2 = getFeedbackTypeSample2();
        assertThat(feedbackType1).isNotEqualTo(feedbackType2);
    }
}
