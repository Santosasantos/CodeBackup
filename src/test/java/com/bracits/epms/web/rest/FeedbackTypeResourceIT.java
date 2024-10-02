package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.FeedbackTypeAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.FeedbackType;
import com.bracits.epms.repository.FeedbackTypeRepository;
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
 * Integration tests for the {@link FeedbackTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeedbackTypeResourceIT {

    private static final String DEFAULT_FEEDBACKNAME = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACKNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feedback-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackTypeRepository feedbackTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackTypeMockMvc;

    private FeedbackType feedbackType;

    private FeedbackType insertedFeedbackType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackType createEntity(EntityManager em) {
        FeedbackType feedbackType = new FeedbackType().feedbackname(DEFAULT_FEEDBACKNAME);
        return feedbackType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackType createUpdatedEntity(EntityManager em) {
        FeedbackType feedbackType = new FeedbackType().feedbackname(UPDATED_FEEDBACKNAME);
        return feedbackType;
    }

    @BeforeEach
    public void initTest() {
        feedbackType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeedbackType != null) {
            feedbackTypeRepository.delete(insertedFeedbackType);
            insertedFeedbackType = null;
        }
    }

    @Test
    @Transactional
    void createFeedbackType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeedbackType
        var returnedFeedbackType = om.readValue(
            restFeedbackTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedbackType.class
        );

        // Validate the FeedbackType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeedbackTypeUpdatableFieldsEquals(returnedFeedbackType, getPersistedFeedbackType(returnedFeedbackType));

        insertedFeedbackType = returnedFeedbackType;
    }

    @Test
    @Transactional
    void createFeedbackTypeWithExistingId() throws Exception {
        // Create the FeedbackType with an existing ID
        feedbackType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackType)))
            .andExpect(status().isBadRequest());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFeedbacknameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackType.setFeedbackname(null);

        // Create the FeedbackType, which fails.

        restFeedbackTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbackTypes() throws Exception {
        // Initialize the database
        insertedFeedbackType = feedbackTypeRepository.saveAndFlush(feedbackType);

        // Get all the feedbackTypeList
        restFeedbackTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackType.getId().intValue())))
            .andExpect(jsonPath("$.[*].feedbackname").value(hasItem(DEFAULT_FEEDBACKNAME)));
    }

    @Test
    @Transactional
    void getFeedbackType() throws Exception {
        // Initialize the database
        insertedFeedbackType = feedbackTypeRepository.saveAndFlush(feedbackType);

        // Get the feedbackType
        restFeedbackTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackType.getId().intValue()))
            .andExpect(jsonPath("$.feedbackname").value(DEFAULT_FEEDBACKNAME));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackType() throws Exception {
        // Get the feedbackType
        restFeedbackTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackType() throws Exception {
        // Initialize the database
        insertedFeedbackType = feedbackTypeRepository.saveAndFlush(feedbackType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackType
        FeedbackType updatedFeedbackType = feedbackTypeRepository.findById(feedbackType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedbackType are not directly saved in db
        em.detach(updatedFeedbackType);
        updatedFeedbackType.feedbackname(UPDATED_FEEDBACKNAME);

        restFeedbackTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeedbackType))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackTypeToMatchAllProperties(updatedFeedbackType);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackType = feedbackTypeRepository.saveAndFlush(feedbackType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackType using partial update
        FeedbackType partialUpdatedFeedbackType = new FeedbackType();
        partialUpdatedFeedbackType.setId(feedbackType.getId());

        restFeedbackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackType))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeedbackType, feedbackType),
            getPersistedFeedbackType(feedbackType)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeedbackTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackType = feedbackTypeRepository.saveAndFlush(feedbackType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackType using partial update
        FeedbackType partialUpdatedFeedbackType = new FeedbackType();
        partialUpdatedFeedbackType.setId(feedbackType.getId());

        partialUpdatedFeedbackType.feedbackname(UPDATED_FEEDBACKNAME);

        restFeedbackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackType))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackTypeUpdatableFieldsEquals(partialUpdatedFeedbackType, getPersistedFeedbackType(partialUpdatedFeedbackType));
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedbackType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackType() throws Exception {
        // Initialize the database
        insertedFeedbackType = feedbackTypeRepository.saveAndFlush(feedbackType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedbackType
        restFeedbackTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackTypeRepository.count();
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

    protected FeedbackType getPersistedFeedbackType(FeedbackType feedbackType) {
        return feedbackTypeRepository.findById(feedbackType.getId()).orElseThrow();
    }

    protected void assertPersistedFeedbackTypeToMatchAllProperties(FeedbackType expectedFeedbackType) {
        assertFeedbackTypeAllPropertiesEquals(expectedFeedbackType, getPersistedFeedbackType(expectedFeedbackType));
    }

    protected void assertPersistedFeedbackTypeToMatchUpdatableProperties(FeedbackType expectedFeedbackType) {
        assertFeedbackTypeAllUpdatablePropertiesEquals(expectedFeedbackType, getPersistedFeedbackType(expectedFeedbackType));
    }
}
