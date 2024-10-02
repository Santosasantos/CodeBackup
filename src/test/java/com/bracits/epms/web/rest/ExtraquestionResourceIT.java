package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.ExtraquestionAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.Extraquestion;
import com.bracits.epms.repository.ExtraquestionRepository;
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
 * Integration tests for the {@link ExtraquestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtraquestionResourceIT {

    private static final String DEFAULT_QUESTION = "AAAAAAAAAA";
    private static final String UPDATED_QUESTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/extraquestions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExtraquestionRepository extraquestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtraquestionMockMvc;

    private Extraquestion extraquestion;

    private Extraquestion insertedExtraquestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extraquestion createEntity(EntityManager em) {
        Extraquestion extraquestion = new Extraquestion().question(DEFAULT_QUESTION);
        return extraquestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Extraquestion createUpdatedEntity(EntityManager em) {
        Extraquestion extraquestion = new Extraquestion().question(UPDATED_QUESTION);
        return extraquestion;
    }

    @BeforeEach
    public void initTest() {
        extraquestion = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedExtraquestion != null) {
            extraquestionRepository.delete(insertedExtraquestion);
            insertedExtraquestion = null;
        }
    }

    @Test
    @Transactional
    void createExtraquestion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Extraquestion
        var returnedExtraquestion = om.readValue(
            restExtraquestionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestion)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Extraquestion.class
        );

        // Validate the Extraquestion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExtraquestionUpdatableFieldsEquals(returnedExtraquestion, getPersistedExtraquestion(returnedExtraquestion));

        insertedExtraquestion = returnedExtraquestion;
    }

    @Test
    @Transactional
    void createExtraquestionWithExistingId() throws Exception {
        // Create the Extraquestion with an existing ID
        extraquestion.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtraquestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestion)))
            .andExpect(status().isBadRequest());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuestionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        extraquestion.setQuestion(null);

        // Create the Extraquestion, which fails.

        restExtraquestionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestion)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExtraquestions() throws Exception {
        // Initialize the database
        insertedExtraquestion = extraquestionRepository.saveAndFlush(extraquestion);

        // Get all the extraquestionList
        restExtraquestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extraquestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].question").value(hasItem(DEFAULT_QUESTION)));
    }

    @Test
    @Transactional
    void getExtraquestion() throws Exception {
        // Initialize the database
        insertedExtraquestion = extraquestionRepository.saveAndFlush(extraquestion);

        // Get the extraquestion
        restExtraquestionMockMvc
            .perform(get(ENTITY_API_URL_ID, extraquestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extraquestion.getId().intValue()))
            .andExpect(jsonPath("$.question").value(DEFAULT_QUESTION));
    }

    @Test
    @Transactional
    void getNonExistingExtraquestion() throws Exception {
        // Get the extraquestion
        restExtraquestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtraquestion() throws Exception {
        // Initialize the database
        insertedExtraquestion = extraquestionRepository.saveAndFlush(extraquestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extraquestion
        Extraquestion updatedExtraquestion = extraquestionRepository.findById(extraquestion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExtraquestion are not directly saved in db
        em.detach(updatedExtraquestion);
        updatedExtraquestion.question(UPDATED_QUESTION);

        restExtraquestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExtraquestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExtraquestion))
            )
            .andExpect(status().isOk());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExtraquestionToMatchAllProperties(updatedExtraquestion);
    }

    @Test
    @Transactional
    void putNonExistingExtraquestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraquestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extraquestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extraquestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtraquestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extraquestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtraquestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extraquestion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExtraquestionWithPatch() throws Exception {
        // Initialize the database
        insertedExtraquestion = extraquestionRepository.saveAndFlush(extraquestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extraquestion using partial update
        Extraquestion partialUpdatedExtraquestion = new Extraquestion();
        partialUpdatedExtraquestion.setId(extraquestion.getId());

        partialUpdatedExtraquestion.question(UPDATED_QUESTION);

        restExtraquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtraquestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtraquestion))
            )
            .andExpect(status().isOk());

        // Validate the Extraquestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtraquestionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExtraquestion, extraquestion),
            getPersistedExtraquestion(extraquestion)
        );
    }

    @Test
    @Transactional
    void fullUpdateExtraquestionWithPatch() throws Exception {
        // Initialize the database
        insertedExtraquestion = extraquestionRepository.saveAndFlush(extraquestion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extraquestion using partial update
        Extraquestion partialUpdatedExtraquestion = new Extraquestion();
        partialUpdatedExtraquestion.setId(extraquestion.getId());

        partialUpdatedExtraquestion.question(UPDATED_QUESTION);

        restExtraquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtraquestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtraquestion))
            )
            .andExpect(status().isOk());

        // Validate the Extraquestion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtraquestionUpdatableFieldsEquals(partialUpdatedExtraquestion, getPersistedExtraquestion(partialUpdatedExtraquestion));
    }

    @Test
    @Transactional
    void patchNonExistingExtraquestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestion.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtraquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extraquestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extraquestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtraquestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extraquestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtraquestion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        extraquestion.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtraquestionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(extraquestion)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Extraquestion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExtraquestion() throws Exception {
        // Initialize the database
        insertedExtraquestion = extraquestionRepository.saveAndFlush(extraquestion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the extraquestion
        restExtraquestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, extraquestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return extraquestionRepository.count();
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

    protected Extraquestion getPersistedExtraquestion(Extraquestion extraquestion) {
        return extraquestionRepository.findById(extraquestion.getId()).orElseThrow();
    }

    protected void assertPersistedExtraquestionToMatchAllProperties(Extraquestion expectedExtraquestion) {
        assertExtraquestionAllPropertiesEquals(expectedExtraquestion, getPersistedExtraquestion(expectedExtraquestion));
    }

    protected void assertPersistedExtraquestionToMatchUpdatableProperties(Extraquestion expectedExtraquestion) {
        assertExtraquestionAllUpdatablePropertiesEquals(expectedExtraquestion, getPersistedExtraquestion(expectedExtraquestion));
    }
}
