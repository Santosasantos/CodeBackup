package com.bracits.epms.domain;

import static com.bracits.epms.domain.RatingScaleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bracits.epms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RatingScaleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RatingScale.class);
        RatingScale ratingScale1 = getRatingScaleSample1();
        RatingScale ratingScale2 = new RatingScale();
        assertThat(ratingScale1).isNotEqualTo(ratingScale2);

        ratingScale2.setId(ratingScale1.getId());
        assertThat(ratingScale1).isEqualTo(ratingScale2);

        ratingScale2 = getRatingScaleSample2();
        assertThat(ratingScale1).isNotEqualTo(ratingScale2);
    }
}
