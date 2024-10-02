package com.bracits.epms.domain;

import static com.bracits.epms.domain.EmployeeTestSamples.*;
import static com.bracits.epms.domain.ExtraquestionTestSamples.*;
import static com.bracits.epms.domain.FeedbackResponderTestSamples.*;
import static com.bracits.epms.domain.FeedbackTestSamples.*;
import static com.bracits.epms.domain.RatingScaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = getFeedbackSample1();
        Feedback feedback2 = new Feedback();
        assertThat(feedback1).isNotEqualTo(feedback2);

        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);

        feedback2 = getFeedbackSample2();
        assertThat(feedback1).isNotEqualTo(feedback2);
    }

    @Test
    void respondersTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        FeedbackResponder feedbackResponderBack = getFeedbackResponderRandomSampleGenerator();

        feedback.addResponders(feedbackResponderBack);
        assertThat(feedback.getResponders()).containsOnly(feedbackResponderBack);
        assertThat(feedbackResponderBack.getFeedback()).isEqualTo(feedback);

        feedback.removeResponders(feedbackResponderBack);
        assertThat(feedback.getResponders()).doesNotContain(feedbackResponderBack);
        assertThat(feedbackResponderBack.getFeedback()).isNull();

        feedback.responders(new HashSet<>(Set.of(feedbackResponderBack)));
        assertThat(feedback.getResponders()).containsOnly(feedbackResponderBack);
        assertThat(feedbackResponderBack.getFeedback()).isEqualTo(feedback);

        feedback.setResponders(new HashSet<>());
        assertThat(feedback.getResponders()).doesNotContain(feedbackResponderBack);
        assertThat(feedbackResponderBack.getFeedback()).isNull();
    }

    @Test
    void extraQuestionsTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        Extraquestion extraquestionBack = getExtraquestionRandomSampleGenerator();

        feedback.addExtraQuestions(extraquestionBack);
        assertThat(feedback.getExtraQuestions()).containsOnly(extraquestionBack);
        assertThat(extraquestionBack.getFeedback()).isEqualTo(feedback);

        feedback.removeExtraQuestions(extraquestionBack);
        assertThat(feedback.getExtraQuestions()).doesNotContain(extraquestionBack);
        assertThat(extraquestionBack.getFeedback()).isNull();

        feedback.extraQuestions(new HashSet<>(Set.of(extraquestionBack)));
        assertThat(feedback.getExtraQuestions()).containsOnly(extraquestionBack);
        assertThat(extraquestionBack.getFeedback()).isEqualTo(feedback);

        feedback.setExtraQuestions(new HashSet<>());
        assertThat(feedback.getExtraQuestions()).doesNotContain(extraquestionBack);
        assertThat(extraquestionBack.getFeedback()).isNull();
    }

    @Test
    void requesterTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        feedback.setRequester(employeeBack);
        assertThat(feedback.getRequester()).isEqualTo(employeeBack);

        feedback.requester(null);
        assertThat(feedback.getRequester()).isNull();
    }

    @Test
    void ratingScaleTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        RatingScale ratingScaleBack = getRatingScaleRandomSampleGenerator();

        feedback.setRatingScale(ratingScaleBack);
        assertThat(feedback.getRatingScale()).isEqualTo(ratingScaleBack);

        feedback.ratingScale(null);
        assertThat(feedback.getRatingScale()).isNull();
    }
}
