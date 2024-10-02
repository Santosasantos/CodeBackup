package com.bracits.epms.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        return new Employee()
            .id(1L)
            .firstname("firstname1")
            .lastname("lastname1")
            .pin("pin1")
            .project("project1")
            .designation("designation1")
            .functionalDesignation("functionalDesignation1")
            .currentOffice("currentOffice1")
            .mobile("mobile1")
            .email("email1")
            .grade(1);
    }

    public static Employee getEmployeeSample2() {
        return new Employee()
            .id(2L)
            .firstname("firstname2")
            .lastname("lastname2")
            .pin("pin2")
            .project("project2")
            .designation("designation2")
            .functionalDesignation("functionalDesignation2")
            .currentOffice("currentOffice2")
            .mobile("mobile2")
            .email("email2")
            .grade(2);
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        return new Employee()
            .id(longCount.incrementAndGet())
            .firstname(UUID.randomUUID().toString())
            .lastname(UUID.randomUUID().toString())
            .pin(UUID.randomUUID().toString())
            .project(UUID.randomUUID().toString())
            .designation(UUID.randomUUID().toString())
            .functionalDesignation(UUID.randomUUID().toString())
            .currentOffice(UUID.randomUUID().toString())
            .mobile(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .grade(intCount.incrementAndGet());
    }
}
