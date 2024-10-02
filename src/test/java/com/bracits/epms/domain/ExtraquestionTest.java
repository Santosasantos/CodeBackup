package com.bracits.epms.domain;

import static com.bracits.epms.domain.ExtraquestionAnsTestSamples.*;
import static com.bracits.epms.domain.ExtraquestionTestSamples.*;
import static com.bracits.epms.domain.FeedbackTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExtraquestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Extraquestion.class);
        Extraquestion extraquestion1 = getExtraquestionSample1();
        Extraquestion extraquestion2 = new Extraquestion();
        assertThat(extraquestion1).isNotEqualTo(extraquestion2);

        extraquestion2.setId(extraquestion1.getId());
        assertThat(extraquestion1).isEqualTo(extraquestion2);

        extraquestion2 = getExtraquestionSample2();
        assertThat(extraquestion1).isNotEqualTo(extraquestion2);
    }

    @Test
    void questionAnsTest() {
        Extraquestion extraquestion = getExtraquestionRandomSampleGenerator();
        ExtraquestionAns extraquestionAnsBack = getExtraquestionAnsRandomSampleGenerator();

        extraquestion.addQuestionAns(extraquestionAnsBack);
        assertThat(extraquestion.getQuestionAns()).containsOnly(extraquestionAnsBack);
        assertThat(extraquestionAnsBack.getQuestions()).isEqualTo(extraquestion);

        extraquestion.removeQuestionAns(extraquestionAnsBack);
        assertThat(extraquestion.getQuestionAns()).doesNotContain(extraquestionAnsBack);
        assertThat(extraquestionAnsBack.getQuestions()).isNull();

        extraquestion.questionAns(new HashSet<>(Set.of(extraquestionAnsBack)));
        assertThat(extraquestion.getQuestionAns()).containsOnly(extraquestionAnsBack);
        assertThat(extraquestionAnsBack.getQuestions()).isEqualTo(extraquestion);

        extraquestion.setQuestionAns(new HashSet<>());
        assertThat(extraquestion.getQuestionAns()).doesNotContain(extraquestionAnsBack);
        assertThat(extraquestionAnsBack.getQuestions()).isNull();
    }

    @Test
    void feedbackTest() {
        Extraquestion extraquestion = getExtraquestionRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        extraquestion.setFeedback(feedbackBack);
        assertThat(extraquestion.getFeedback()).isEqualTo(feedbackBack);

        extraquestion.feedback(null);
        assertThat(extraquestion.getFeedback()).isNull();
    }
}
