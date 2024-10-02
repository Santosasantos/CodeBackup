package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TeachOtherTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TeachOther getTeachOtherSample1() {
        return new TeachOther()
            .id(1L)
            .technicalSkill("technicalSkill1")
            .particularStrengh("particularStrengh1")
            .whynotRecommend("whynotRecommend1");
    }

    public static TeachOther getTeachOtherSample2() {
        return new TeachOther()
            .id(2L)
            .technicalSkill("technicalSkill2")
            .particularStrengh("particularStrengh2")
            .whynotRecommend("whynotRecommend2");
    }

    public static TeachOther getTeachOtherRandomSampleGenerator() {
        return new TeachOther()
            .id(longCount.incrementAndGet())
            .technicalSkill(UUID.randomUUID().toString())
            .particularStrengh(UUID.randomUUID().toString())
            .whynotRecommend(UUID.randomUUID().toString());
    }
}
