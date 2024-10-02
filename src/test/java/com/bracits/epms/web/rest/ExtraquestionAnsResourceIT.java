package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.ExtraquestionAnsAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.ExtraquestionAns;
import com.bracits.epms.repository.ExtraquestionAnsRepository;
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
 * Integration tests for the {@link ExtraquestionAnsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtraquestionAnsResourceIT {

    private static final String DEFAULT_QUESTIONANS = "AAAAAAAAAA";
    private static final String UPDATED_QUESTIONANS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/extraquestion-ans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExtraquestionAnsRepository extraquestionAnsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtraquestionAnsMockMvc;

    private ExtraquestionAns extraquestionAns;

    private ExtraquestionAns insertedExtraquestionAns;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraquestionAns createEntity(EntityManager em) {
        ExtraquestionAns extraquestionAns = new ExtraquestionAns().questionans(DEFAULT_QUESTIONANS);
        return extraquestionAns;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtraquestionAns createUpdatedEntity(EntityManager em) {
        ExtraquestionAns extraquestionAns = new ExtraquestionAns().questionans(UPDATED_QUESTIONANS);
        return extraquestionAns;
    }

    @BeforeEach
    public void initTest() {
        extraquestionAns = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedExtraquestionAns != null) {
            extraquestionAnsRepository.delete(insertedExtraquestionAns);
            insertedExtraquestionAns = null;
        }
    }

    @Test
    @Transactional
    void createExtraquestionAns() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExtraquestionAns
        var returnedExtraquestionAns = om.readValue(
            restExtraquestionAnsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestionAns)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExtraquestionAns.class
        );

        // Validate the ExtraquestionAns in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExtraquestionAnsUpdatableFieldsEquals(returnedExtraquestionAns, getPersistedExtraquestionAns(returnedExtraquestionAns));

        insertedExtraquestionAns = returnedExtraquestionAns;
    }

    @Test
    @Transactional
    void createExtraquestionAnsWithExistingId() throws Exception {
        // Create the ExtraquestionAns with an existing ID
        extraquestionAns.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraquestionAnsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestionAns)))
            .andExpect(status().isBadRequest());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuestionansIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        extraquestionAns.setQuestionans(null);

        // Create the ExtraquestionAns, which fails.

        restExtraquestionAnsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestionAns)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExtraquestionAns() throws Exception {
        // Initialize the database
        insertedExtraquestionAns = extraquestionAnsRepository.saveAndFlush(extraquestionAns);

        // Get all the extraquestionAnsList
        restExtraquestionAnsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraquestionAns.getId().intValue())))
            .andExpect(jsonPath("$.[*].questionans").value(hasItem(DEFAULT_QUESTIONANS)));
    }

    @Test
    @Transactional
    void getExtraquestionAns() throws Exception {
        // Initialize the database
        insertedExtraquestionAns = extraquestionAnsRepository.saveAndFlush(extraquestionAns);

        // Get the extraquestionAns
        restExtraquestionAnsMockMvc
            .perform(get(ENTITY_API_URL_ID, extraquestionAns.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extraquestionAns.getId().intValue()))
            .andExpect(jsonPath("$.questionans").value(DEFAULT_QUESTIONANS));
    }

    @Test
    @Transactional
    void getNonExistingExtraquestionAns() throws Exception {
        // Get the extraquestionAns
        restExtraquestionAnsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtraquestionAns() throws Exception {
        // Initialize the database
        insertedExtraquestionAns = extraquestionAnsRepository.saveAndFlush(extraquestionAns);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extraquestionAns
        ExtraquestionAns updatedExtraquestionAns = extraquestionAnsRepository.findById(extraquestionAns.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExtraquestionAns are not directly saved in db
        em.detach(updatedExtraquestionAns);
        updatedExtraquestionAns.questionans(UPDATED_QUESTIONANS);

        restExtraquestionAnsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtraquestionAns.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExtraquestionAns))
            )
            .andExpect(status().isOk());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExtraquestionAnsToMatchAllProperties(updatedExtraquestionAns);
    }

    @Test
    @Transactional
    void putNonExistingExtraquestionAns() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestionAns.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraquestionAnsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extraquestionAns.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extraquestionAns))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtraquestionAns() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestionAns.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionAnsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extraquestionAns))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtraquestionAns() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestionAns.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionAnsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestionAns)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtraquestionAnsWithPatch() throws Exception {
        // Initialize the database
        insertedExtraquestionAns = extraquestionAnsRepository.saveAndFlush(extraquestionAns);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extraquestionAns using partial update
        ExtraquestionAns partialUpdatedExtraquestionAns = new ExtraquestionAns();
        partialUpdatedExtraquestionAns.setId(extraquestionAns.getId());

        restExtraquestionAnsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtraquestionAns.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtraquestionAns))
            )
            .andExpect(status().isOk());

        // Validate the ExtraquestionAns in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtraquestionAnsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExtraquestionAns, extraquestionAns),
            getPersistedExtraquestionAns(extraquestionAns)
        );
    }

    @Test
    @Transactional
    void fullUpdateExtraquestionAnsWithPatch() throws Exception {
        // Initialize the database
        insertedExtraquestionAns = extraquestionAnsRepository.saveAndFlush(extraquestionAns);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extraquestionAns using partial update
        ExtraquestionAns partialUpdatedExtraquestionAns = new ExtraquestionAns();
        partialUpdatedExtraquestionAns.setId(extraquestionAns.getId());

        partialUpdatedExtraquestionAns.questionans(UPDATED_QUESTIONANS);

        restExtraquestionAnsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtraquestionAns.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtraquestionAns))
            )
            .andExpect(status().isOk());

        // Validate the ExtraquestionAns in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtraquestionAnsUpdatableFieldsEquals(
            partialUpdatedExtraquestionAns,
            getPersistedExtraquestionAns(partialUpdatedExtraquestionAns)
        );
    }

    @Test
    @Transactional
    void patchNonExistingExtraquestionAns() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestionAns.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraquestionAnsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extraquestionAns.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extraquestionAns))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtraquestionAns() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestionAns.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionAnsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extraquestionAns))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtraquestionAns() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestionAns.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionAnsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(extraquestionAns)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtraquestionAns in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtraquestionAns() throws Exception {
        // Initialize the database
        insertedExtraquestionAns = extraquestionAnsRepository.saveAndFlush(extraquestionAns);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the extraquestionAns
        restExtraquestionAnsMockMvc
            .perform(delete(ENTITY_API_URL_ID, extraquestionAns.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return extraquestionAnsRepository.count();
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

    protected ExtraquestionAns getPersistedExtraquestionAns(ExtraquestionAns extraquestionAns) {
        return extraquestionAnsRepository.findById(extraquestionAns.getId()).orElseThrow();
    }

    protected void assertPersistedExtraquestionAnsToMatchAllProperties(ExtraquestionAns expectedExtraquestionAns) {
        assertExtraquestionAnsAllPropertiesEquals(expectedExtraquestionAns, getPersistedExtraquestionAns(expectedExtraquestionAns));
    }

    protected void assertPersistedExtraquestionAnsToMatchUpdatableProperties(ExtraquestionAns expectedExtraquestionAns) {
        assertExtraquestionAnsAllUpdatablePropertiesEquals(
            expectedExtraquestionAns,
            getPersistedExtraquestionAns(expectedExtraquestionAns)
        );
    }
}
