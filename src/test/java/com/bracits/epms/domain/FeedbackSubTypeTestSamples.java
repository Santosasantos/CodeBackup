package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackSubTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeedbackSubType getFeedbackSubTypeSample1() {
        return new FeedbackSubType().id(1L).feedbacksubname("feedbacksubname1").feedbackdescription("feedbackdescription1");
    }

    public static FeedbackSubType getFeedbackSubTypeSample2() {
        return new FeedbackSubType().id(2L).feedbacksubname("feedbacksubname2").feedbackdescription("feedbackdescription2");
    }

    public static FeedbackSubType getFeedbackSubTypeRandomSampleGenerator() {
        return new FeedbackSubType()
            .id(longCount.incrementAndGet())
            .feedbacksubname(UUID.randomUUID().toString())
            .feedbackdescription(UUID.randomUUID().toString());
    }
}
