package com.bracits.epms.domain;

import static com.bracits.epms.domain.FeedbackResponderTestSamples.*;
import static com.bracits.epms.domain.SkillDevelopmentDetailsTestSamples.*;
import static com.bracits.epms.domain.SkillDevelopmentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SkillDevelopmentDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillDevelopmentDetails.class);
        SkillDevelopmentDetails skillDevelopmentDetails1 = getSkillDevelopmentDetailsSample1();
        SkillDevelopmentDetails skillDevelopmentDetails2 = new SkillDevelopmentDetails();
        assertThat(skillDevelopmentDetails1).isNotEqualTo(skillDevelopmentDetails2);

        skillDevelopmentDetails2.setId(skillDevelopmentDetails1.getId());
        assertThat(skillDevelopmentDetails1).isEqualTo(skillDevelopmentDetails2);

        skillDevelopmentDetails2 = getSkillDevelopmentDetailsSample2();
        assertThat(skillDevelopmentDetails1).isNotEqualTo(skillDevelopmentDetails2);
    }

    @Test
    void skillDevelopmentTypeTest() {
        SkillDevelopmentDetails skillDevelopmentDetails = getSkillDevelopmentDetailsRandomSampleGenerator();
        SkillDevelopmentType skillDevelopmentTypeBack = getSkillDevelopmentTypeRandomSampleGenerator();

        skillDevelopmentDetails.setSkillDevelopmentType(skillDevelopmentTypeBack);
        assertThat(skillDevelopmentDetails.getSkillDevelopmentType()).isEqualTo(skillDevelopmentTypeBack);

        skillDevelopmentDetails.skillDevelopmentType(null);
        assertThat(skillDevelopmentDetails.getSkillDevelopmentType()).isNull();
    }

    @Test
    void responderTest() {
        SkillDevelopmentDetails skillDevelopmentDetails = getSkillDevelopmentDetailsRandomSampleGenerator();
        FeedbackResponder feedbackResponderBack = getFeedbackResponderRandomSampleGenerator();

        skillDevelopmentDetails.setResponder(feedbackResponderBack);
        assertThat(skillDevelopmentDetails.getResponder()).isEqualTo(feedbackResponderBack);

        skillDevelopmentDetails.responder(null);
        assertThat(skillDevelopmentDetails.getResponder()).isNull();
    }
}
