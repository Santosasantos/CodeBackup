package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ExtraquestionAnsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ExtraquestionAns getExtraquestionAnsSample1() {
        return new ExtraquestionAns().id(1L).questionans("questionans1");
    }

    public static ExtraquestionAns getExtraquestionAnsSample2() {
        return new ExtraquestionAns().id(2L).questionans("questionans2");
    }

    public static ExtraquestionAns getExtraquestionAnsRandomSampleGenerator() {
        return new ExtraquestionAns().id(longCount.incrementAndGet()).questionans(UUID.randomUUID().toString());
    }
}
