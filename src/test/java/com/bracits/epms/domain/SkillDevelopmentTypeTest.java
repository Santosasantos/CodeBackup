package com.bracits.epms.domain;

import static com.bracits.epms.domain.SkillDevelopmentTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SkillDevelopmentTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillDevelopmentType.class);
        SkillDevelopmentType skillDevelopmentType1 = getSkillDevelopmentTypeSample1();
        SkillDevelopmentType skillDevelopmentType2 = new SkillDevelopmentType();
        assertThat(skillDevelopmentType1).isNotEqualTo(skillDevelopmentType2);

        skillDevelopmentType2.setId(skillDevelopmentType1.getId());
        assertThat(skillDevelopmentType1).isEqualTo(skillDevelopmentType2);

        skillDevelopmentType2 = getSkillDevelopmentTypeSample2();
        assertThat(skillDevelopmentType1).isNotEqualTo(skillDevelopmentType2);
    }
}
