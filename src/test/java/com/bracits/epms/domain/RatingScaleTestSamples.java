package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RatingScaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RatingScale getRatingScaleSample1() {
        return new RatingScale().id(1L).scaletype("scaletype1").ratingscales("ratingscales1");
    }

    public static RatingScale getRatingScaleSample2() {
        return new RatingScale().id(2L).scaletype("scaletype2").ratingscales("ratingscales2");
    }

    public static RatingScale getRatingScaleRandomSampleGenerator() {
        return new RatingScale()
            .id(longCount.incrementAndGet())
            .scaletype(UUID.randomUUID().toString())
            .ratingscales(UUID.randomUUID().toString());
    }
}
