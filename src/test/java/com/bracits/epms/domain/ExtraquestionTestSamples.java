package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExtraquestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Extraquestion getExtraquestionSample1() {
        return new Extraquestion().id(1L).question("question1");
    }

    public static Extraquestion getExtraquestionSample2() {
        return new Extraquestion().id(2L).question("question2");
    }

    public static Extraquestion getExtraquestionRandomSampleGenerator() {
        return new Extraquestion().id(longCount.incrementAndGet()).question(UUID.randomUUID().toString());
    }
}
