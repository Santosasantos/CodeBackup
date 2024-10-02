package com.bracits.epms.domain;

import static com.bracits.epms.domain.ExtraquestionAnsTestSamples.*;
import static com.bracits.epms.domain.ExtraquestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExtraquestionAnsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtraquestionAns.class);
        ExtraquestionAns extraquestionAns1 = getExtraquestionAnsSample1();
        ExtraquestionAns extraquestionAns2 = new ExtraquestionAns();
        assertThat(extraquestionAns1).isNotEqualTo(extraquestionAns2);

        extraquestionAns2.setId(extraquestionAns1.getId());
        assertThat(extraquestionAns1).isEqualTo(extraquestionAns2);

        extraquestionAns2 = getExtraquestionAnsSample2();
        assertThat(extraquestionAns1).isNotEqualTo(extraquestionAns2);
    }

    @Test
    void questionsTest() {
        ExtraquestionAns extraquestionAns = getExtraquestionAnsRandomSampleGenerator();
        Extraquestion extraquestionBack = getExtraquestionRandomSampleGenerator();

        extraquestionAns.setQuestions(extraquestionBack);
        assertThat(extraquestionAns.getQuestions()).isEqualTo(extraquestionBack);

        extraquestionAns.questions(null);
        assertThat(extraquestionAns.getQuestions()).isNull();
    }
}
