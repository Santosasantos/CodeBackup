package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeedbackType getFeedbackTypeSample1() {
        return new FeedbackType().id(1L).feedbackname("feedbackname1");
    }

    public static FeedbackType getFeedbackTypeSample2() {
        return new FeedbackType().id(2L).feedbackname("feedbackname2");
    }

    public static FeedbackType getFeedbackTypeRandomSampleGenerator() {
        return new FeedbackType().id(longCount.incrementAndGet()).feedbackname(UUID.randomUUID().toString());
    }
}
