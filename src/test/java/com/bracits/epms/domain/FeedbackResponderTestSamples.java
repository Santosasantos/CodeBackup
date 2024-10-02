package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackResponderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static FeedbackResponder getFeedbackResponderSample1() {
        return new FeedbackResponder().id(1L).stakeholderEmail("stakeholderEmail1");
    }

    public static FeedbackResponder getFeedbackResponderSample2() {
        return new FeedbackResponder().id(2L).stakeholderEmail("stakeholderEmail2");
    }

    public static FeedbackResponder getFeedbackResponderRandomSampleGenerator() {
        return new FeedbackResponder().id(longCount.incrementAndGet()).stakeholderEmail(UUID.randomUUID().toString());
    }
}
