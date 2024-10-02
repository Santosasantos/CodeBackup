package com.bracits.epms.domain;

import static com.bracits.epms.domain.FeedbackResponderTestSamples.*;
import static com.bracits.epms.domain.TeachOtherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TeachOtherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeachOther.class);
        TeachOther teachOther1 = getTeachOtherSample1();
        TeachOther teachOther2 = new TeachOther();
        assertThat(teachOther1).isNotEqualTo(teachOther2);

        teachOther2.setId(teachOther1.getId());
        assertThat(teachOther1).isEqualTo(teachOther2);

        teachOther2 = getTeachOtherSample2();
        assertThat(teachOther1).isNotEqualTo(teachOther2);
    }

    @Test
    void responderTest() {
        TeachOther teachOther = getTeachOtherRandomSampleGenerator();
        FeedbackResponder feedbackResponderBack = getFeedbackResponderRandomSampleGenerator();

        teachOther.setResponder(feedbackResponderBack);
        assertThat(teachOther.getResponder()).isEqualTo(feedbackResponderBack);

        teachOther.responder(null);
        assertThat(teachOther.getResponder()).isNull();
    }
}
