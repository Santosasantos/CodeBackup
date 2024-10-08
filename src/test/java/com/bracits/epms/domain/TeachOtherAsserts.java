package com.bracits.epms.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TeachOtherAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachOtherAllPropertiesEquals(TeachOther expected, TeachOther actual) {
        assertTeachOtherAutoGeneratedPropertiesEquals(expected, actual);
        assertTeachOtherAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachOtherAllUpdatablePropertiesEquals(TeachOther expected, TeachOther actual) {
        assertTeachOtherUpdatableFieldsEquals(expected, actual);
        assertTeachOtherUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachOtherAutoGeneratedPropertiesEquals(TeachOther expected, TeachOther actual) {
        assertThat(expected)
            .as("Verify TeachOther auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachOtherUpdatableFieldsEquals(TeachOther expected, TeachOther actual) {
        assertThat(expected)
            .as("Verify TeachOther relevant properties")
            .satisfies(e -> assertThat(e.getTechnicalSkill()).as("check technicalSkill").isEqualTo(actual.getTechnicalSkill()))
            .satisfies(e -> assertThat(e.getRecommendation()).as("check recommendation").isEqualTo(actual.getRecommendation()))
            .satisfies(e -> assertThat(e.getParticularStrengh()).as("check particularStrengh").isEqualTo(actual.getParticularStrengh()))
            .satisfies(e -> assertThat(e.getWhynotRecommend()).as("check whynotRecommend").isEqualTo(actual.getWhynotRecommend()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTeachOtherUpdatableRelationshipsEquals(TeachOther expected, TeachOther actual) {
        assertThat(expected)
            .as("Verify TeachOther relationships")
            .satisfies(e -> assertThat(e.getResponder()).as("check responder").isEqualTo(actual.getResponder()));
    }
}
