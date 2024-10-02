package com.bracits.epms.domain;

import static com.bracits.epms.domain.EmployeeTestSamples.*;
import static com.bracits.epms.domain.FeedbackDetailsTestSamples.*;
import static com.bracits.epms.domain.FeedbackResponderTestSamples.*;
import static com.bracits.epms.domain.FeedbackTestSamples.*;
import static com.bracits.epms.domain.SkillDevelopmentDetailsTestSamples.*;
import static com.bracits.epms.domain.TeachOtherTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FeedbackResponderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeedbackResponder.class);
        FeedbackResponder feedbackResponder1 = getFeedbackResponderSample1();
        FeedbackResponder feedbackResponder2 = new FeedbackResponder();
        assertThat(feedbackResponder1).isNotEqualTo(feedbackResponder2);

        feedbackResponder2.setId(feedbackResponder1.getId());
        assertThat(feedbackResponder1).isEqualTo(feedbackResponder2);

        feedbackResponder2 = getFeedbackResponderSample2();
        assertThat(feedbackResponder1).isNotEqualTo(feedbackResponder2);
    }

    @Test
    void feedbackDetailsTest() {
        FeedbackResponder feedbackResponder = getFeedbackResponderRandomSampleGenerator();
        FeedbackDetails feedbackDetailsBack = getFeedbackDetailsRandomSampleGenerator();

        feedbackResponder.addFeedbackDetails(feedbackDetailsBack);
        assertThat(feedbackResponder.getFeedbackDetails()).containsOnly(feedbackDetailsBack);
        assertThat(feedbackDetailsBack.getResponder()).isEqualTo(feedbackResponder);

        feedbackResponder.removeFeedbackDetails(feedbackDetailsBack);
        assertThat(feedbackResponder.getFeedbackDetails()).doesNotContain(feedbackDetailsBack);
        assertThat(feedbackDetailsBack.getResponder()).isNull();

        feedbackResponder.feedbackDetails(new HashSet<>(Set.of(feedbackDetailsBack)));
        assertThat(feedbackResponder.getFeedbackDetails()).containsOnly(feedbackDetailsBack);
        assertThat(feedbackDetailsBack.getResponder()).isEqualTo(feedbackResponder);

        feedbackResponder.setFeedbackDetails(new HashSet<>());
        assertThat(feedbackResponder.getFeedbackDetails()).doesNotContain(feedbackDetailsBack);
        assertThat(feedbackDetailsBack.getResponder()).isNull();
    }

    @Test
    void teachOthersTest() {
        FeedbackResponder feedbackResponder = getFeedbackResponderRandomSampleGenerator();
        TeachOther teachOtherBack = getTeachOtherRandomSampleGenerator();

        feedbackResponder.addTeachOthers(teachOtherBack);
        assertThat(feedbackResponder.getTeachOthers()).containsOnly(teachOtherBack);
        assertThat(teachOtherBack.getResponder()).isEqualTo(feedbackResponder);

        feedbackResponder.removeTeachOthers(teachOtherBack);
        assertThat(feedbackResponder.getTeachOthers()).doesNotContain(teachOtherBack);
        assertThat(teachOtherBack.getResponder()).isNull();

        feedbackResponder.teachOthers(new HashSet<>(Set.of(teachOtherBack)));
        assertThat(feedbackResponder.getTeachOthers()).containsOnly(teachOtherBack);
        assertThat(teachOtherBack.getResponder()).isEqualTo(feedbackResponder);

        feedbackResponder.setTeachOthers(new HashSet<>());
        assertThat(feedbackResponder.getTeachOthers()).doesNotContain(teachOtherBack);
        assertThat(teachOtherBack.getResponder()).isNull();
    }

    @Test
    void skillDevelopmentDetailsTest() {
        FeedbackResponder feedbackResponder = getFeedbackResponderRandomSampleGenerator();
        SkillDevelopmentDetails skillDevelopmentDetailsBack = getSkillDevelopmentDetailsRandomSampleGenerator();

        feedbackResponder.addSkillDevelopmentDetails(skillDevelopmentDetailsBack);
        assertThat(feedbackResponder.getSkillDevelopmentDetails()).containsOnly(skillDevelopmentDetailsBack);
        assertThat(skillDevelopmentDetailsBack.getResponder()).isEqualTo(feedbackResponder);

        feedbackResponder.removeSkillDevelopmentDetails(skillDevelopmentDetailsBack);
        assertThat(feedbackResponder.getSkillDevelopmentDetails()).doesNotContain(skillDevelopmentDetailsBack);
        assertThat(skillDevelopmentDetailsBack.getResponder()).isNull();

        feedbackResponder.skillDevelopmentDetails(new HashSet<>(Set.of(skillDevelopmentDetailsBack)));
        assertThat(feedbackResponder.getSkillDevelopmentDetails()).containsOnly(skillDevelopmentDetailsBack);
        assertThat(skillDevelopmentDetailsBack.getResponder()).isEqualTo(feedbackResponder);

        feedbackResponder.setSkillDevelopmentDetails(new HashSet<>());
        assertThat(feedbackResponder.getSkillDevelopmentDetails()).doesNotContain(skillDevelopmentDetailsBack);
        assertThat(skillDevelopmentDetailsBack.getResponder()).isNull();
    }

    @Test
    void employeeTest() {
        FeedbackResponder feedbackResponder = getFeedbackResponderRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        feedbackResponder.setEmployee(employeeBack);
        assertThat(feedbackResponder.getEmployee()).isEqualTo(employeeBack);

        feedbackResponder.employee(null);
        assertThat(feedbackResponder.getEmployee()).isNull();
    }

    @Test
    void feedbackTest() {
        FeedbackResponder feedbackResponder = getFeedbackResponderRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        feedbackResponder.setFeedback(feedbackBack);
        assertThat(feedbackResponder.getFeedback()).isEqualTo(feedbackBack);

        feedbackResponder.feedback(null);
        assertThat(feedbackResponder.getFeedback()).isNull();
    }
}
