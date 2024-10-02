package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SkillDevelopmentTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SkillDevelopmentType getSkillDevelopmentTypeSample1() {
        return new SkillDevelopmentType().id(1L).skilldevelopmentname("skilldevelopmentname1");
    }

    public static SkillDevelopmentType getSkillDevelopmentTypeSample2() {
        return new SkillDevelopmentType().id(2L).skilldevelopmentname("skilldevelopmentname2");
    }

    public static SkillDevelopmentType getSkillDevelopmentTypeRandomSampleGenerator() {
        return new SkillDevelopmentType().id(longCount.incrementAndGet()).skilldevelopmentname(UUID.randomUUID().toString());
    }
}
