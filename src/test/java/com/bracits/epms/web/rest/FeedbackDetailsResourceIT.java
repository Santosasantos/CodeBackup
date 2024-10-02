package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.FeedbackDetailsAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.FeedbackDetails;
import com.bracits.epms.repository.FeedbackDetailsRepository;
import com.bracits.epms.service.FeedbackDetailsService;
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
 * Integration tests for the {@link FeedbackDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeedbackDetailsResourceIT {

    private static final String DEFAULT_COMMENTSFORFEEDBACKSUBTYPE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTSFORFEEDBACKSUBTYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATINGVALUE = 1;
    private static final Integer UPDATED_RATINGVALUE = 2;

    private static final String ENTITY_API_URL = "/api/feedback-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackDetailsRepository feedbackDetailsRepository;

    @Mock
    private FeedbackDetailsRepository feedbackDetailsRepositoryMock;

    @Mock
    private FeedbackDetailsService feedbackDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackDetailsMockMvc;

    private FeedbackDetails feedbackDetails;

    private FeedbackDetails insertedFeedbackDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackDetails createEntity(EntityManager em) {
        FeedbackDetails feedbackDetails = new FeedbackDetails()
            .commentsforfeedbacksubtype(DEFAULT_COMMENTSFORFEEDBACKSUBTYPE)
            .ratingvalue(DEFAULT_RATINGVALUE);
        return feedbackDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackDetails createUpdatedEntity(EntityManager em) {
        FeedbackDetails feedbackDetails = new FeedbackDetails()
            .commentsforfeedbacksubtype(UPDATED_COMMENTSFORFEEDBACKSUBTYPE)
            .ratingvalue(UPDATED_RATINGVALUE);
        return feedbackDetails;
    }

    @BeforeEach
    public void initTest() {
        feedbackDetails = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeedbackDetails != null) {
            feedbackDetailsRepository.delete(insertedFeedbackDetails);
            insertedFeedbackDetails = null;
        }
    }

    @Test
    @Transactional
    void createFeedbackDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeedbackDetails
        var returnedFeedbackDetails = om.readValue(
            restFeedbackDetailsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDetails)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedbackDetails.class
        );

        // Validate the FeedbackDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeedbackDetailsUpdatableFieldsEquals(returnedFeedbackDetails, getPersistedFeedbackDetails(returnedFeedbackDetails));

        insertedFeedbackDetails = returnedFeedbackDetails;
    }

    @Test
    @Transactional
    void createFeedbackDetailsWithExistingId() throws Exception {
        // Create the FeedbackDetails with an existing ID
        feedbackDetails.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDetails)))
            .andExpect(status().isBadRequest());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFeedbackDetails() throws Exception {
        // Initialize the database
        insertedFeedbackDetails = feedbackDetailsRepository.saveAndFlush(feedbackDetails);

        // Get all the feedbackDetailsList
        restFeedbackDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].commentsforfeedbacksubtype").value(hasItem(DEFAULT_COMMENTSFORFEEDBACKSUBTYPE)))
            .andExpect(jsonPath("$.[*].ratingvalue").value(hasItem(DEFAULT_RATINGVALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(feedbackDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feedbackDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feedbackDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feedbackDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeedbackDetails() throws Exception {
        // Initialize the database
        insertedFeedbackDetails = feedbackDetailsRepository.saveAndFlush(feedbackDetails);

        // Get the feedbackDetails
        restFeedbackDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackDetails.getId().intValue()))
            .andExpect(jsonPath("$.commentsforfeedbacksubtype").value(DEFAULT_COMMENTSFORFEEDBACKSUBTYPE))
            .andExpect(jsonPath("$.ratingvalue").value(DEFAULT_RATINGVALUE));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackDetails() throws Exception {
        // Get the feedbackDetails
        restFeedbackDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackDetails() throws Exception {
        // Initialize the database
        insertedFeedbackDetails = feedbackDetailsRepository.saveAndFlush(feedbackDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackDetails
        FeedbackDetails updatedFeedbackDetails = feedbackDetailsRepository.findById(feedbackDetails.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedbackDetails are not directly saved in db
        em.detach(updatedFeedbackDetails);
        updatedFeedbackDetails.commentsforfeedbacksubtype(UPDATED_COMMENTSFORFEEDBACKSUBTYPE).ratingvalue(UPDATED_RATINGVALUE);

        restFeedbackDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeedbackDetails))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackDetailsToMatchAllProperties(updatedFeedbackDetails);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackDetails = feedbackDetailsRepository.saveAndFlush(feedbackDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackDetails using partial update
        FeedbackDetails partialUpdatedFeedbackDetails = new FeedbackDetails();
        partialUpdatedFeedbackDetails.setId(feedbackDetails.getId());

        partialUpdatedFeedbackDetails.commentsforfeedbacksubtype(UPDATED_COMMENTSFORFEEDBACKSUBTYPE);

        restFeedbackDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackDetails))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeedbackDetails, feedbackDetails),
            getPersistedFeedbackDetails(feedbackDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeedbackDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackDetails = feedbackDetailsRepository.saveAndFlush(feedbackDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackDetails using partial update
        FeedbackDetails partialUpdatedFeedbackDetails = new FeedbackDetails();
        partialUpdatedFeedbackDetails.setId(feedbackDetails.getId());

        partialUpdatedFeedbackDetails.commentsforfeedbacksubtype(UPDATED_COMMENTSFORFEEDBACKSUBTYPE).ratingvalue(UPDATED_RATINGVALUE);

        restFeedbackDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackDetails))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackDetailsUpdatableFieldsEquals(
            partialUpdatedFeedbackDetails,
            getPersistedFeedbackDetails(partialUpdatedFeedbackDetails)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackDetailsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedbackDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackDetails() throws Exception {
        // Initialize the database
        insertedFeedbackDetails = feedbackDetailsRepository.saveAndFlush(feedbackDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedbackDetails
        restFeedbackDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackDetailsRepository.count();
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

    protected FeedbackDetails getPersistedFeedbackDetails(FeedbackDetails feedbackDetails) {
        return feedbackDetailsRepository.findById(feedbackDetails.getId()).orElseThrow();
    }

    protected void assertPersistedFeedbackDetailsToMatchAllProperties(FeedbackDetails expectedFeedbackDetails) {
        assertFeedbackDetailsAllPropertiesEquals(expectedFeedbackDetails, getPersistedFeedbackDetails(expectedFeedbackDetails));
    }

    protected void assertPersistedFeedbackDetailsToMatchUpdatableProperties(FeedbackDetails expectedFeedbackDetails) {
        assertFeedbackDetailsAllUpdatablePropertiesEquals(expectedFeedbackDetails, getPersistedFeedbackDetails(expectedFeedbackDetails));
    }
}
