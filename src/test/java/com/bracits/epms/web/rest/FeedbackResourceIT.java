package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.FeedbackAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.Feedback;
import com.bracits.epms.domain.enumeration.FeedbackStatus;
import com.bracits.epms.repository.FeedbackRepository;
import com.bracits.epms.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link FeedbackResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FeedbackResourceIT {

    private static final Instant DEFAULT_REQUEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final FeedbackStatus DEFAULT_STATUS = FeedbackStatus.NEW;
    private static final FeedbackStatus UPDATED_STATUS = FeedbackStatus.SENT_TO_SUPERVISOR;

    private static final LocalDate DEFAULT_RESPONSE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RESPONSE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Integer DEFAULT_ASSESSMENT_YEAR = 1;
    private static final Integer UPDATED_ASSESSMENT_YEAR = 2;

    private static final String ENTITY_API_URL = "/api/feedbacks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Mock
    private FeedbackRepository feedbackRepositoryMock;

    @Mock
    private FeedbackService feedbackServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeedbackMockMvc;

    private Feedback feedback;

    private Feedback insertedFeedback;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .requestDate(DEFAULT_REQUEST_DATE)
            .status(DEFAULT_STATUS)
            .responseDate(DEFAULT_RESPONSE_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .assessmentYear(DEFAULT_ASSESSMENT_YEAR);
        return feedback;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feedback createUpdatedEntity(EntityManager em) {
        Feedback feedback = new Feedback()
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .responseDate(UPDATED_RESPONSE_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .assessmentYear(UPDATED_ASSESSMENT_YEAR);
        return feedback;
    }

    @BeforeEach
    public void initTest() {
        feedback = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedFeedback != null) {
            feedbackRepository.delete(insertedFeedback);
            insertedFeedback = null;
        }
    }

    @Test
    @Transactional
    void createFeedback() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Feedback
        var returnedFeedback = om.readValue(
            restFeedbackMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Feedback.class
        );

        // Validate the Feedback in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFeedbackUpdatableFieldsEquals(returnedFeedback, getPersistedFeedback(returnedFeedback));

        insertedFeedback = returnedFeedback;
    }

    @Test
    @Transactional
    void createFeedbackWithExistingId() throws Exception {
        // Create the Feedback with an existing ID
        feedback.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback)))
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRequestDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setRequestDate(null);

        // Create the Feedback, which fails.

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setStatus(null);

        // Create the Feedback, which fails.

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setCreatedBy(null);

        // Create the Feedback, which fails.

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssessmentYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feedback.setAssessmentYear(null);

        // Create the Feedback, which fails.

        restFeedbackMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeedbacks() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get all the feedbackList
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feedback.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].responseDate").value(hasItem(DEFAULT_RESPONSE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].assessmentYear").value(hasItem(DEFAULT_ASSESSMENT_YEAR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbacksWithEagerRelationshipsIsEnabled() throws Exception {
        when(feedbackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(feedbackServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFeedbacksWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(feedbackServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFeedbackMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(feedbackRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        // Get the feedback
        restFeedbackMockMvc
            .perform(get(ENTITY_API_URL_ID, feedback.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feedback.getId().intValue()))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.responseDate").value(DEFAULT_RESPONSE_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.assessmentYear").value(DEFAULT_ASSESSMENT_YEAR));
    }

    @Test
    @Transactional
    void getNonExistingFeedback() throws Exception {
        // Get the feedback
        restFeedbackMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback
        Feedback updatedFeedback = feedbackRepository.findById(feedback.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeedback are not directly saved in db
        em.detach(updatedFeedback);
        updatedFeedback
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .responseDate(UPDATED_RESPONSE_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .assessmentYear(UPDATED_ASSESSMENT_YEAR);

        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFeedback.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeedbackToMatchAllProperties(updatedFeedback);
    }

    @Test
    @Transactional
    void putNonExistingFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feedback.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feedback))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feedback)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .responseDate(UPDATED_RESPONSE_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .assessmentYear(UPDATED_ASSESSMENT_YEAR);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeedback, feedback), getPersistedFeedback(feedback));
    }

    @Test
    @Transactional
    void fullUpdateFeedbackWithPatch() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feedback using partial update
        Feedback partialUpdatedFeedback = new Feedback();
        partialUpdatedFeedback.setId(feedback.getId());

        partialUpdatedFeedback
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .responseDate(UPDATED_RESPONSE_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .assessmentYear(UPDATED_ASSESSMENT_YEAR);

        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeedback))
            )
            .andExpect(status().isOk());

        // Validate the Feedback in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeedbackUpdatableFieldsEquals(partialUpdatedFeedback, getPersistedFeedback(partialUpdatedFeedback));
    }

    @Test
    @Transactional
    void patchNonExistingFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feedback.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedback))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feedback))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeedback() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feedback.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeedbackMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feedback)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feedback in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeedback() throws Exception {
        // Initialize the database
        insertedFeedback = feedbackRepository.saveAndFlush(feedback);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feedback
        restFeedbackMockMvc
            .perform(delete(ENTITY_API_URL_ID, feedback.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feedbackRepository.count();
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

    protected Feedback getPersistedFeedback(Feedback feedback) {
        return feedbackRepository.findById(feedback.getId()).orElseThrow();
    }

    protected void assertPersistedFeedbackToMatchAllProperties(Feedback expectedFeedback) {
        assertFeedbackAllPropertiesEquals(expectedFeedback, getPersistedFeedback(expectedFeedback));
    }

    protected void assertPersistedFeedbackToMatchUpdatableProperties(Feedback expectedFeedback) {
        assertFeedbackAllUpdatablePropertiesEquals(expectedFeedback, getPersistedFeedback(expectedFeedback));
    }
}
