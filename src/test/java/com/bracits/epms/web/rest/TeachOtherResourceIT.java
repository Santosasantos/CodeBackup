package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.TeachOtherAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.TeachOther;
import com.bracits.epms.domain.enumeration.RecommendationValue;
import com.bracits.epms.repository.TeachOtherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TeachOtherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TeachOtherResourceIT {

    private static final String DEFAULT_TECHNICAL_SKILL = "AAAAAAAAAA";
    private static final String UPDATED_TECHNICAL_SKILL = "BBBBBBBBBB";

    private static final RecommendationValue DEFAULT_RECOMMENDATION = RecommendationValue.Yes;
    private static final RecommendationValue UPDATED_RECOMMENDATION = RecommendationValue.No;

    private static final String DEFAULT_PARTICULAR_STRENGH = "AAAAAAAAAA";
    private static final String UPDATED_PARTICULAR_STRENGH = "BBBBBBBBBB";

    private static final String DEFAULT_WHYNOT_RECOMMEND = "AAAAAAAAAA";
    private static final String UPDATED_WHYNOT_RECOMMEND = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/teach-others";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TeachOtherRepository teachOtherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTeachOtherMockMvc;

    private TeachOther teachOther;

    private TeachOther insertedTeachOther;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachOther createEntity(EntityManager em) {
        TeachOther teachOther = new TeachOther()
            .technicalSkill(DEFAULT_TECHNICAL_SKILL)
            .recommendation(DEFAULT_RECOMMENDATION)
            .particularStrengh(DEFAULT_PARTICULAR_STRENGH)
            .whynotRecommend(DEFAULT_WHYNOT_RECOMMEND);
        return teachOther;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachOther createUpdatedEntity(EntityManager em) {
        TeachOther teachOther = new TeachOther()
            .technicalSkill(UPDATED_TECHNICAL_SKILL)
            .recommendation(UPDATED_RECOMMENDATION)
            .particularStrengh(UPDATED_PARTICULAR_STRENGH)
            .whynotRecommend(UPDATED_WHYNOT_RECOMMEND);
        return teachOther;
    }

    @BeforeEach
    public void initTest() {
        teachOther = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTeachOther != null) {
            teachOtherRepository.delete(insertedTeachOther);
            insertedTeachOther = null;
        }
    }

    @Test
    @Transactional
    void createTeachOther() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TeachOther
        var returnedTeachOther = om.readValue(
            restTeachOtherMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachOther)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TeachOther.class
        );

        // Validate the TeachOther in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTeachOtherUpdatableFieldsEquals(returnedTeachOther, getPersistedTeachOther(returnedTeachOther));

        insertedTeachOther = returnedTeachOther;
    }

    @Test
    @Transactional
    void createTeachOtherWithExistingId() throws Exception {
        // Create the TeachOther with an existing ID
        teachOther.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeachOtherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachOther)))
            .andExpect(status().isBadRequest());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTechnicalSkillIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teachOther.setTechnicalSkill(null);

        // Create the TeachOther, which fails.

        restTeachOtherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachOther)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecommendationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        teachOther.setRecommendation(null);

        // Create the TeachOther, which fails.

        restTeachOtherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachOther)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTeachOthers() throws Exception {
        // Initialize the database
        insertedTeachOther = teachOtherRepository.saveAndFlush(teachOther);

        // Get all the teachOtherList
        restTeachOtherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teachOther.getId().intValue())))
            .andExpect(jsonPath("$.[*].technicalSkill").value(hasItem(DEFAULT_TECHNICAL_SKILL)))
            .andExpect(jsonPath("$.[*].recommendation").value(hasItem(DEFAULT_RECOMMENDATION.toString())))
            .andExpect(jsonPath("$.[*].particularStrengh").value(hasItem(DEFAULT_PARTICULAR_STRENGH)))
            .andExpect(jsonPath("$.[*].whynotRecommend").value(hasItem(DEFAULT_WHYNOT_RECOMMEND)));
    }

    @Test
    @Transactional
    void getTeachOther() throws Exception {
        // Initialize the database
        insertedTeachOther = teachOtherRepository.saveAndFlush(teachOther);

        // Get the teachOther
        restTeachOtherMockMvc
            .perform(get(ENTITY_API_URL_ID, teachOther.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(teachOther.getId().intValue()))
            .andExpect(jsonPath("$.technicalSkill").value(DEFAULT_TECHNICAL_SKILL))
            .andExpect(jsonPath("$.recommendation").value(DEFAULT_RECOMMENDATION.toString()))
            .andExpect(jsonPath("$.particularStrengh").value(DEFAULT_PARTICULAR_STRENGH))
            .andExpect(jsonPath("$.whynotRecommend").value(DEFAULT_WHYNOT_RECOMMEND));
    }

    @Test
    @Transactional
    void getNonExistingTeachOther() throws Exception {
        // Get the teachOther
        restTeachOtherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTeachOther() throws Exception {
        // Initialize the database
        insertedTeachOther = teachOtherRepository.saveAndFlush(teachOther);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachOther
        TeachOther updatedTeachOther = teachOtherRepository.findById(teachOther.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTeachOther are not directly saved in db
        em.detach(updatedTeachOther);
        updatedTeachOther
            .technicalSkill(UPDATED_TECHNICAL_SKILL)
            .recommendation(UPDATED_RECOMMENDATION)
            .particularStrengh(UPDATED_PARTICULAR_STRENGH)
            .whynotRecommend(UPDATED_WHYNOT_RECOMMEND);

        restTeachOtherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTeachOther.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTeachOther))
            )
            .andExpect(status().isOk());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTeachOtherToMatchAllProperties(updatedTeachOther);
    }

    @Test
    @Transactional
    void putNonExistingTeachOther() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachOther.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachOtherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, teachOther.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachOther))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTeachOther() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachOther.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachOtherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(teachOther))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTeachOther() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachOther.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachOtherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(teachOther)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTeachOtherWithPatch() throws Exception {
        // Initialize the database
        insertedTeachOther = teachOtherRepository.saveAndFlush(teachOther);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachOther using partial update
        TeachOther partialUpdatedTeachOther = new TeachOther();
        partialUpdatedTeachOther.setId(teachOther.getId());

        partialUpdatedTeachOther.technicalSkill(UPDATED_TECHNICAL_SKILL).whynotRecommend(UPDATED_WHYNOT_RECOMMEND);

        restTeachOtherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeachOther.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeachOther))
            )
            .andExpect(status().isOk());

        // Validate the TeachOther in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeachOtherUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTeachOther, teachOther),
            getPersistedTeachOther(teachOther)
        );
    }

    @Test
    @Transactional
    void fullUpdateTeachOtherWithPatch() throws Exception {
        // Initialize the database
        insertedTeachOther = teachOtherRepository.saveAndFlush(teachOther);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the teachOther using partial update
        TeachOther partialUpdatedTeachOther = new TeachOther();
        partialUpdatedTeachOther.setId(teachOther.getId());

        partialUpdatedTeachOther
            .technicalSkill(UPDATED_TECHNICAL_SKILL)
            .recommendation(UPDATED_RECOMMENDATION)
            .particularStrengh(UPDATED_PARTICULAR_STRENGH)
            .whynotRecommend(UPDATED_WHYNOT_RECOMMEND);

        restTeachOtherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTeachOther.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTeachOther))
            )
            .andExpect(status().isOk());

        // Validate the TeachOther in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTeachOtherUpdatableFieldsEquals(partialUpdatedTeachOther, getPersistedTeachOther(partialUpdatedTeachOther));
    }

    @Test
    @Transactional
    void patchNonExistingTeachOther() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachOther.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachOtherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, teachOther.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teachOther))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTeachOther() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachOther.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachOtherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(teachOther))
            )
            .andExpect(status().isBadRequest());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTeachOther() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        teachOther.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTeachOtherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(teachOther)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TeachOther in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTeachOther() throws Exception {
        // Initialize the database
        insertedTeachOther = teachOtherRepository.saveAndFlush(teachOther);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the teachOther
        restTeachOtherMockMvc
            .perform(delete(ENTITY_API_URL_ID, teachOther.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return teachOtherRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TeachOther getPersistedTeachOther(TeachOther teachOther) {
        return teachOtherRepository.findById(teachOther.getId()).orElseThrow();
    }

    protected void assertPersistedTeachOtherToMatchAllProperties(TeachOther expectedTeachOther) {
        assertTeachOtherAllPropertiesEquals(expectedTeachOther, getPersistedTeachOther(expectedTeachOther));
    }

    protected void assertPersistedTeachOtherToMatchUpdatableProperties(TeachOther expectedTeachOther) {
        assertTeachOtherAllUpdatablePropertiesEquals(expectedTeachOther, getPersistedTeachOther(expectedTeachOther));
    }
}
