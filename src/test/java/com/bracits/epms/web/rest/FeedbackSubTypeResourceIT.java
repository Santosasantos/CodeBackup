package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.FeedbackSubTypeAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.FeedbackSubType;
import com.bracits.epms.repository.FeedbackSubTypeRepository;
import com.bracits.epms.service.FeedbackSubTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FeedbackSubTypeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeedbackSubTypeResourceIT {

    private static final String DEFAULT_FEEDBACKSUBNAME = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACKSUBNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FEEDBACKDESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_FEEDBACKDESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/feedback-sub-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackSubTypeRepository feedbackSubTypeRepository;

    @Mock
    private FeedbackSubTypeRepository feedbackSubTypeRepositoryMock;

    @Mock
    private FeedbackSubTypeService feedbackSubTypeServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackSubTypeMockMvc;

    private FeedbackSubType feedbackSubType;

    private FeedbackSubType insertedFeedbackSubType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackSubType createEntity(EntityManager em) {
        FeedbackSubType feedbackSubType = new FeedbackSubType()
            .feedbacksubname(DEFAULT_FEEDBACKSUBNAME)
            .feedbackdescription(DEFAULT_FEEDBACKDESCRIPTION);
        return feedbackSubType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackSubType createUpdatedEntity(EntityManager em) {
        FeedbackSubType feedbackSubType = new FeedbackSubType()
            .feedbacksubname(UPDATED_FEEDBACKSUBNAME)
            .feedbackdescription(UPDATED_FEEDBACKDESCRIPTION);
        return feedbackSubType;
    }

    @BeforeEach
    public void initTest() {
        feedbackSubType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeedbackSubType != null) {
            feedbackSubTypeRepository.delete(insertedFeedbackSubType);
            insertedFeedbackSubType = null;
        }
    }

    @Test
    @Transactional
    void createFeedbackSubType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeedbackSubType
        var returnedFeedbackSubType = om.readValue(
            restFeedbackSubTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackSubType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedbackSubType.class
        );

        // Validate the FeedbackSubType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeedbackSubTypeUpdatableFieldsEquals(returnedFeedbackSubType, getPersistedFeedbackSubType(returnedFeedbackSubType));

        insertedFeedbackSubType = returnedFeedbackSubType;
    }

    @Test
    @Transactional
    void createFeedbackSubTypeWithExistingId() throws Exception {
        // Create the FeedbackSubType with an existing ID
        feedbackSubType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackSubTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackSubType)))
            .andExpect(status().isBadRequest());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFeedbacksubnameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackSubType.setFeedbacksubname(null);

        // Create the FeedbackSubType, which fails.

        restFeedbackSubTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackSubType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFeedbackdescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackSubType.setFeedbackdescription(null);

        // Create the FeedbackSubType, which fails.

        restFeedbackSubTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackSubType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbackSubTypes() throws Exception {
        // Initialize the database
        insertedFeedbackSubType = feedbackSubTypeRepository.saveAndFlush(feedbackSubType);

        // Get all the feedbackSubTypeList
        restFeedbackSubTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackSubType.getId().intValue())))
            .andExpect(jsonPath("$.[*].feedbacksubname").value(hasItem(DEFAULT_FEEDBACKSUBNAME)))
            .andExpect(jsonPath("$.[*].feedbackdescription").value(hasItem(DEFAULT_FEEDBACKDESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackSubTypesWithEagerRelationshipsIsEnabled() throws Exception {
        when(feedbackSubTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackSubTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feedbackSubTypeServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackSubTypesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feedbackSubTypeServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackSubTypeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feedbackSubTypeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeedbackSubType() throws Exception {
        // Initialize the database
        insertedFeedbackSubType = feedbackSubTypeRepository.saveAndFlush(feedbackSubType);

        // Get the feedbackSubType
        restFeedbackSubTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackSubType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackSubType.getId().intValue()))
            .andExpect(jsonPath("$.feedbacksubname").value(DEFAULT_FEEDBACKSUBNAME))
            .andExpect(jsonPath("$.feedbackdescription").value(DEFAULT_FEEDBACKDESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackSubType() throws Exception {
        // Get the feedbackSubType
        restFeedbackSubTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackSubType() throws Exception {
        // Initialize the database
        insertedFeedbackSubType = feedbackSubTypeRepository.saveAndFlush(feedbackSubType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackSubType
        FeedbackSubType updatedFeedbackSubType = feedbackSubTypeRepository.findById(feedbackSubType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedbackSubType are not directly saved in db
        em.detach(updatedFeedbackSubType);
        updatedFeedbackSubType.feedbacksubname(UPDATED_FEEDBACKSUBNAME).feedbackdescription(UPDATED_FEEDBACKDESCRIPTION);

        restFeedbackSubTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackSubType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeedbackSubType))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackSubTypeToMatchAllProperties(updatedFeedbackSubType);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackSubType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackSubType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackSubTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackSubType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackSubType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackSubType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackSubType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackSubTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackSubType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackSubType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackSubType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackSubTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackSubType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackSubTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackSubType = feedbackSubTypeRepository.saveAndFlush(feedbackSubType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackSubType using partial update
        FeedbackSubType partialUpdatedFeedbackSubType = new FeedbackSubType();
        partialUpdatedFeedbackSubType.setId(feedbackSubType.getId());

        partialUpdatedFeedbackSubType.feedbacksubname(UPDATED_FEEDBACKSUBNAME).feedbackdescription(UPDATED_FEEDBACKDESCRIPTION);

        restFeedbackSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackSubType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackSubType))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackSubType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackSubTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeedbackSubType, feedbackSubType),
            getPersistedFeedbackSubType(feedbackSubType)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeedbackSubTypeWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackSubType = feedbackSubTypeRepository.saveAndFlush(feedbackSubType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackSubType using partial update
        FeedbackSubType partialUpdatedFeedbackSubType = new FeedbackSubType();
        partialUpdatedFeedbackSubType.setId(feedbackSubType.getId());

        partialUpdatedFeedbackSubType.feedbacksubname(UPDATED_FEEDBACKSUBNAME).feedbackdescription(UPDATED_FEEDBACKDESCRIPTION);

        restFeedbackSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackSubType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackSubType))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackSubType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackSubTypeUpdatableFieldsEquals(
            partialUpdatedFeedbackSubType,
            getPersistedFeedbackSubType(partialUpdatedFeedbackSubType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackSubType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackSubType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackSubType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackSubType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackSubType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackSubType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackSubTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackSubType))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackSubType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackSubType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackSubTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedbackSubType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackSubType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackSubType() throws Exception {
        // Initialize the database
        insertedFeedbackSubType = feedbackSubTypeRepository.saveAndFlush(feedbackSubType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedbackSubType
        restFeedbackSubTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackSubType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackSubTypeRepository.count();
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

    protected FeedbackSubType getPersistedFeedbackSubType(FeedbackSubType feedbackSubType) {
        return feedbackSubTypeRepository.findById(feedbackSubType.getId()).orElseThrow();
    }

    protected void assertPersistedFeedbackSubTypeToMatchAllProperties(FeedbackSubType expectedFeedbackSubType) {
        assertFeedbackSubTypeAllPropertiesEquals(expectedFeedbackSubType, getPersistedFeedbackSubType(expectedFeedbackSubType));
    }

    protected void assertPersistedFeedbackSubTypeToMatchUpdatableProperties(FeedbackSubType expectedFeedbackSubType) {
        assertFeedbackSubTypeAllUpdatablePropertiesEquals(expectedFeedbackSubType, getPersistedFeedbackSubType(expectedFeedbackSubType));
    }
}
