package com.bracits.epms.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SkillDevelopmentDetailsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SkillDevelopmentDetails getSkillDevelopmentDetailsSample1() {
        return new SkillDevelopmentDetails().id(1L);
    }

    public static SkillDevelopmentDetails getSkillDevelopmentDetailsSample2() {
        return new SkillDevelopmentDetails().id(2L);
    }

    public static SkillDevelopmentDetails getSkillDevelopmentDetailsRandomSampleGenerator() {
        return new SkillDevelopmentDetails().id(longCount.incrementAndGet());
    }
}
