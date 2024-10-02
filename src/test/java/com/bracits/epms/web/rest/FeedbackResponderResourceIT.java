package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.FeedbackResponderAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.FeedbackResponder;
import com.bracits.epms.domain.enumeration.FeedbackStatus;
import com.bracits.epms.domain.enumeration.ResponderCategory;
import com.bracits.epms.repository.FeedbackResponderRepository;
import com.bracits.epms.service.FeedbackResponderService;
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
 * Integration tests for the {@link FeedbackResponderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeedbackResponderResourceIT {

    private static final ResponderCategory DEFAULT_CATEGORY = ResponderCategory.SELF;
    private static final ResponderCategory UPDATED_CATEGORY = ResponderCategory.SUPERVISOR;

    private static final String DEFAULT_STAKEHOLDER_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_STAKEHOLDER_EMAIL = "BBBBBBBBBB";

    private static final FeedbackStatus DEFAULT_RESPONDER_STATUS = FeedbackStatus.NEW;
    private static final FeedbackStatus UPDATED_RESPONDER_STATUS = FeedbackStatus.SENT_TO_SUPERVISOR;

    private static final String ENTITY_API_URL = "/api/feedback-responders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackResponderRepository feedbackResponderRepository;

    @Mock
    private FeedbackResponderRepository feedbackResponderRepositoryMock;

    @Mock
    private FeedbackResponderService feedbackResponderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackResponderMockMvc;

    private FeedbackResponder feedbackResponder;

    private FeedbackResponder insertedFeedbackResponder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponder createEntity(EntityManager em) {
        FeedbackResponder feedbackResponder = new FeedbackResponder()
            .category(DEFAULT_CATEGORY)
            .stakeholderEmail(DEFAULT_STAKEHOLDER_EMAIL)
            .responderStatus(DEFAULT_RESPONDER_STATUS);
        return feedbackResponder;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeedbackResponder createUpdatedEntity(EntityManager em) {
        FeedbackResponder feedbackResponder = new FeedbackResponder()
            .category(UPDATED_CATEGORY)
            .stakeholderEmail(UPDATED_STAKEHOLDER_EMAIL)
            .responderStatus(UPDATED_RESPONDER_STATUS);
        return feedbackResponder;
    }

    @BeforeEach
    public void initTest() {
        feedbackResponder = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeedbackResponder != null) {
            feedbackResponderRepository.delete(insertedFeedbackResponder);
            insertedFeedbackResponder = null;
        }
    }

    @Test
    @Transactional
    void createFeedbackResponder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FeedbackResponder
        var returnedFeedbackResponder = om.readValue(
            restFeedbackResponderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackResponder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeedbackResponder.class
        );

        // Validate the FeedbackResponder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeedbackResponderUpdatableFieldsEquals(returnedFeedbackResponder, getPersistedFeedbackResponder(returnedFeedbackResponder));

        insertedFeedbackResponder = returnedFeedbackResponder;
    }

    @Test
    @Transactional
    void createFeedbackResponderWithExistingId() throws Exception {
        // Create the FeedbackResponder with an existing ID
        feedbackResponder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackResponderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackResponder)))
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCategoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackResponder.setCategory(null);

        // Create the FeedbackResponder, which fails.

        restFeedbackResponderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackResponder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkResponderStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedbackResponder.setResponderStatus(null);

        // Create the FeedbackResponder, which fails.

        restFeedbackResponderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackResponder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbackResponders() throws Exception {
        // Initialize the database
        insertedFeedbackResponder = feedbackResponderRepository.saveAndFlush(feedbackResponder);

        // Get all the feedbackResponderList
        restFeedbackResponderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedbackResponder.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].stakeholderEmail").value(hasItem(DEFAULT_STAKEHOLDER_EMAIL)))
            .andExpect(jsonPath("$.[*].responderStatus").value(hasItem(DEFAULT_RESPONDER_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackRespondersWithEagerRelationshipsIsEnabled() throws Exception {
        when(feedbackResponderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackResponderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feedbackResponderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbackRespondersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feedbackResponderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackResponderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feedbackResponderRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeedbackResponder() throws Exception {
        // Initialize the database
        insertedFeedbackResponder = feedbackResponderRepository.saveAndFlush(feedbackResponder);

        // Get the feedbackResponder
        restFeedbackResponderMockMvc
            .perform(get(ENTITY_API_URL_ID, feedbackResponder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedbackResponder.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.stakeholderEmail").value(DEFAULT_STAKEHOLDER_EMAIL))
            .andExpect(jsonPath("$.responderStatus").value(DEFAULT_RESPONDER_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFeedbackResponder() throws Exception {
        // Get the feedbackResponder
        restFeedbackResponderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedbackResponder() throws Exception {
        // Initialize the database
        insertedFeedbackResponder = feedbackResponderRepository.saveAndFlush(feedbackResponder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackResponder
        FeedbackResponder updatedFeedbackResponder = feedbackResponderRepository.findById(feedbackResponder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedbackResponder are not directly saved in db
        em.detach(updatedFeedbackResponder);
        updatedFeedbackResponder
            .category(UPDATED_CATEGORY)
            .stakeholderEmail(UPDATED_STAKEHOLDER_EMAIL)
            .responderStatus(UPDATED_RESPONDER_STATUS);

        restFeedbackResponderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedbackResponder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeedbackResponder))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackResponderToMatchAllProperties(updatedFeedbackResponder);
    }

    @Test
    @Transactional
    void putNonExistingFeedbackResponder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackResponderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedbackResponder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackResponder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedbackResponder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedbackResponder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedbackResponder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedbackResponder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackResponderWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackResponder = feedbackResponderRepository.saveAndFlush(feedbackResponder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackResponder using partial update
        FeedbackResponder partialUpdatedFeedbackResponder = new FeedbackResponder();
        partialUpdatedFeedbackResponder.setId(feedbackResponder.getId());

        partialUpdatedFeedbackResponder.stakeholderEmail(UPDATED_STAKEHOLDER_EMAIL).responderStatus(UPDATED_RESPONDER_STATUS);

        restFeedbackResponderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackResponder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackResponder))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackResponder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackResponderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFeedbackResponder, feedbackResponder),
            getPersistedFeedbackResponder(feedbackResponder)
        );
    }

    @Test
    @Transactional
    void fullUpdateFeedbackResponderWithPatch() throws Exception {
        // Initialize the database
        insertedFeedbackResponder = feedbackResponderRepository.saveAndFlush(feedbackResponder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedbackResponder using partial update
        FeedbackResponder partialUpdatedFeedbackResponder = new FeedbackResponder();
        partialUpdatedFeedbackResponder.setId(feedbackResponder.getId());

        partialUpdatedFeedbackResponder
            .category(UPDATED_CATEGORY)
            .stakeholderEmail(UPDATED_STAKEHOLDER_EMAIL)
            .responderStatus(UPDATED_RESPONDER_STATUS);

        restFeedbackResponderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedbackResponder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedbackResponder))
            )
            .andExpect(status().isOk());

        // Validate the FeedbackResponder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackResponderUpdatableFieldsEquals(
            partialUpdatedFeedbackResponder,
            getPersistedFeedbackResponder(partialUpdatedFeedbackResponder)
        );
    }

    @Test
    @Transactional
    void patchNonExistingFeedbackResponder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackResponderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedbackResponder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackResponder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedbackResponder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedbackResponder))
            )
            .andExpect(status().isBadRequest());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedbackResponder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedbackResponder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackResponderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedbackResponder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FeedbackResponder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedbackResponder() throws Exception {
        // Initialize the database
        insertedFeedbackResponder = feedbackResponderRepository.saveAndFlush(feedbackResponder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedbackResponder
        restFeedbackResponderMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedbackResponder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackResponderRepository.count();
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

    protected FeedbackResponder getPersistedFeedbackResponder(FeedbackResponder feedbackResponder) {
        return feedbackResponderRepository.findById(feedbackResponder.getId()).orElseThrow();
    }

    protected void assertPersistedFeedbackResponderToMatchAllProperties(FeedbackResponder expectedFeedbackResponder) {
        assertFeedbackResponderAllPropertiesEquals(expectedFeedbackResponder, getPersistedFeedbackResponder(expectedFeedbackResponder));
    }

    protected void assertPersistedFeedbackResponderToMatchUpdatableProperties(FeedbackResponder expectedFeedbackResponder) {
        assertFeedbackResponderAllUpdatablePropertiesEquals(
            expectedFeedbackResponder,
            getPersistedFeedbackResponder(expectedFeedbackResponder)
        );
    }
}
