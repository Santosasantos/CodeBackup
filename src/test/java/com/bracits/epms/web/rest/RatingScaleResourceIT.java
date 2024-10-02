package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.RatingScaleAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.RatingScale;
import com.bracits.epms.repository.RatingScaleRepository;
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
 * Integration tests for the {@link RatingScaleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RatingScaleResourceIT {

    private static final String DEFAULT_SCALETYPE = "AAAAAAAAAA";
    private static final String UPDATED_SCALETYPE = "BBBBBBBBBB";

    private static final String DEFAULT_RATINGSCALES = "AAAAAAAAAA";
    private static final String UPDATED_RATINGSCALES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rating-scales";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RatingScaleRepository ratingScaleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRatingScaleMockMvc;

    private RatingScale ratingScale;

    private RatingScale insertedRatingScale;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RatingScale createEntity(EntityManager em) {
        RatingScale ratingScale = new RatingScale().scaletype(DEFAULT_SCALETYPE).ratingscales(DEFAULT_RATINGSCALES);
        return ratingScale;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RatingScale createUpdatedEntity(EntityManager em) {
        RatingScale ratingScale = new RatingScale().scaletype(UPDATED_SCALETYPE).ratingscales(UPDATED_RATINGSCALES);
        return ratingScale;
    }

    @BeforeEach
    public void initTest() {
        ratingScale = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedRatingScale != null) {
            ratingScaleRepository.delete(insertedRatingScale);
            insertedRatingScale = null;
        }
    }

    @Test
    @Transactional
    void createRatingScale() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RatingScale
        var returnedRatingScale = om.readValue(
            restRatingScaleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingScale)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RatingScale.class
        );

        // Validate the RatingScale in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRatingScaleUpdatableFieldsEquals(returnedRatingScale, getPersistedRatingScale(returnedRatingScale));

        insertedRatingScale = returnedRatingScale;
    }

    @Test
    @Transactional
    void createRatingScaleWithExistingId() throws Exception {
        // Create the RatingScale with an existing ID
        ratingScale.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRatingScaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingScale)))
            .andExpect(status().isBadRequest());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScaletypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ratingScale.setScaletype(null);

        // Create the RatingScale, which fails.

        restRatingScaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingScale)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRatingscalesIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ratingScale.setRatingscales(null);

        // Create the RatingScale, which fails.

        restRatingScaleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingScale)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRatingScales() throws Exception {
        // Initialize the database
        insertedRatingScale = ratingScaleRepository.saveAndFlush(ratingScale);

        // Get all the ratingScaleList
        restRatingScaleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ratingScale.getId().intValue())))
            .andExpect(jsonPath("$.[*].scaletype").value(hasItem(DEFAULT_SCALETYPE)))
            .andExpect(jsonPath("$.[*].ratingscales").value(hasItem(DEFAULT_RATINGSCALES)));
    }

    @Test
    @Transactional
    void getRatingScale() throws Exception {
        // Initialize the database
        insertedRatingScale = ratingScaleRepository.saveAndFlush(ratingScale);

        // Get the ratingScale
        restRatingScaleMockMvc
            .perform(get(ENTITY_API_URL_ID, ratingScale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ratingScale.getId().intValue()))
            .andExpect(jsonPath("$.scaletype").value(DEFAULT_SCALETYPE))
            .andExpect(jsonPath("$.ratingscales").value(DEFAULT_RATINGSCALES));
    }

    @Test
    @Transactional
    void getNonExistingRatingScale() throws Exception {
        // Get the ratingScale
        restRatingScaleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRatingScale() throws Exception {
        // Initialize the database
        insertedRatingScale = ratingScaleRepository.saveAndFlush(ratingScale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ratingScale
        RatingScale updatedRatingScale = ratingScaleRepository.findById(ratingScale.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRatingScale are not directly saved in db
        em.detach(updatedRatingScale);
        updatedRatingScale.scaletype(UPDATED_SCALETYPE).ratingscales(UPDATED_RATINGSCALES);

        restRatingScaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRatingScale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRatingScale))
            )
            .andExpect(status().isOk());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRatingScaleToMatchAllProperties(updatedRatingScale);
    }

    @Test
    @Transactional
    void putNonExistingRatingScale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingScale.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingScaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ratingScale.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ratingScale))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRatingScale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingScale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingScaleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ratingScale))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRatingScale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingScale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingScaleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ratingScale)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRatingScaleWithPatch() throws Exception {
        // Initialize the database
        insertedRatingScale = ratingScaleRepository.saveAndFlush(ratingScale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ratingScale using partial update
        RatingScale partialUpdatedRatingScale = new RatingScale();
        partialUpdatedRatingScale.setId(ratingScale.getId());

        restRatingScaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRatingScale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRatingScale))
            )
            .andExpect(status().isOk());

        // Validate the RatingScale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingScaleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRatingScale, ratingScale),
            getPersistedRatingScale(ratingScale)
        );
    }

    @Test
    @Transactional
    void fullUpdateRatingScaleWithPatch() throws Exception {
        // Initialize the database
        insertedRatingScale = ratingScaleRepository.saveAndFlush(ratingScale);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ratingScale using partial update
        RatingScale partialUpdatedRatingScale = new RatingScale();
        partialUpdatedRatingScale.setId(ratingScale.getId());

        partialUpdatedRatingScale.scaletype(UPDATED_SCALETYPE).ratingscales(UPDATED_RATINGSCALES);

        restRatingScaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRatingScale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRatingScale))
            )
            .andExpect(status().isOk());

        // Validate the RatingScale in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRatingScaleUpdatableFieldsEquals(partialUpdatedRatingScale, getPersistedRatingScale(partialUpdatedRatingScale));
    }

    @Test
    @Transactional
    void patchNonExistingRatingScale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingScale.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRatingScaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ratingScale.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingScale))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRatingScale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingScale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingScaleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ratingScale))
            )
            .andExpect(status().isBadRequest());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRatingScale() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ratingScale.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRatingScaleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ratingScale)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RatingScale in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRatingScale() throws Exception {
        // Initialize the database
        insertedRatingScale = ratingScaleRepository.saveAndFlush(ratingScale);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ratingScale
        restRatingScaleMockMvc
            .perform(delete(ENTITY_API_URL_ID, ratingScale.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ratingScaleRepository.count();
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

    protected RatingScale getPersistedRatingScale(RatingScale ratingScale) {
        return ratingScaleRepository.findById(ratingScale.getId()).orElseThrow();
    }

    protected void assertPersistedRatingScaleToMatchAllProperties(RatingScale expectedRatingScale) {
        assertRatingScaleAllPropertiesEquals(expectedRatingScale, getPersistedRatingScale(expectedRatingScale));
    }

    protected void assertPersistedRatingScaleToMatchUpdatableProperties(RatingScale expectedRatingScale) {
        assertRatingScaleAllUpdatablePropertiesEquals(expectedRatingScale, getPersistedRatingScale(expectedRatingScale));
    }
}
