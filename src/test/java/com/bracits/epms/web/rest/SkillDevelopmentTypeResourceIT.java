package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.SkillDevelopmentTypeAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.SkillDevelopmentType;
import com.bracits.epms.repository.SkillDevelopmentTypeRepository;
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
 * Integration tests for the {@link SkillDevelopmentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SkillDevelopmentTypeResourceIT {

    private static final String DEFAULT_SKILLDEVELOPMENTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SKILLDEVELOPMENTNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/skill-development-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillDevelopmentTypeRepository skillDevelopmentTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillDevelopmentTypeMockMvc;

    private SkillDevelopmentType skillDevelopmentType;

    private SkillDevelopmentType insertedSkillDevelopmentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillDevelopmentType createEntity(EntityManager em) {
        SkillDevelopmentType skillDevelopmentType = new SkillDevelopmentType().skilldevelopmentname(DEFAULT_SKILLDEVELOPMENTNAME);
        return skillDevelopmentType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillDevelopmentType createUpdatedEntity(EntityManager em) {
        SkillDevelopmentType skillDevelopmentType = new SkillDevelopmentType().skilldevelopmentname(UPDATED_SKILLDEVELOPMENTNAME);
        return skillDevelopmentType;
    }

    @BeforeEach
    public void initTest() {
        skillDevelopmentType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSkillDevelopmentType != null) {
            skillDevelopmentTypeRepository.delete(insertedSkillDevelopmentType);
            insertedSkillDevelopmentType = null;
        }
    }

    @Test
    @Transactional
    void createSkillDevelopmentType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SkillDevelopmentType
        var returnedSkillDevelopmentType = om.readValue(
            restSkillDevelopmentTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDevelopmentType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SkillDevelopmentType.class
        );

        // Validate the SkillDevelopmentType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSkillDevelopmentTypeUpdatableFieldsEquals(
            returnedSkillDevelopmentType,
            getPersistedSkillDevelopmentType(returnedSkillDevelopmentType)
        );

        insertedSkillDevelopmentType = returnedSkillDevelopmentType;
    }

    @Test
    @Transactional
    void createSkillDevelopmentTypeWithExistingId() throws Exception {
        // Create the SkillDevelopmentType with an existing ID
        skillDevelopmentType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillDevelopmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDevelopmentType)))
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSkilldevelopmentnameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        skillDevelopmentType.setSkilldevelopmentname(null);

        // Create the SkillDevelopmentType, which fails.

        restSkillDevelopmentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDevelopmentType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSkillDevelopmentTypes() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentType = skillDevelopmentTypeRepository.saveAndFlush(skillDevelopmentType);

        // Get all the skillDevelopmentTypeList
        restSkillDevelopmentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillDevelopmentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].skilldevelopmentname").value(hasItem(DEFAULT_SKILLDEVELOPMENTNAME)));
    }

    @Test
    @Transactional
    void getSkillDevelopmentType() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentType = skillDevelopmentTypeRepository.saveAndFlush(skillDevelopmentType);

        // Get the skillDevelopmentType
        restSkillDevelopmentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, skillDevelopmentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skillDevelopmentType.getId().intValue()))
            .andExpect(jsonPath("$.skilldevelopmentname").value(DEFAULT_SKILLDEVELOPMENTNAME));
    }

    @Test
    @Transactional
    void getNonExistingSkillDevelopmentType() throws Exception {
        // Get the skillDevelopmentType
        restSkillDevelopmentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkillDevelopmentType() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentType = skillDevelopmentTypeRepository.saveAndFlush(skillDevelopmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillDevelopmentType
        SkillDevelopmentType updatedSkillDevelopmentType = skillDevelopmentTypeRepository
            .findById(skillDevelopmentType.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSkillDevelopmentType are not directly saved in db
        em.detach(updatedSkillDevelopmentType);
        updatedSkillDevelopmentType.skilldevelopmentname(UPDATED_SKILLDEVELOPMENTNAME);

        restSkillDevelopmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSkillDevelopmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSkillDevelopmentType))
            )
            .andExpect(status().isOk());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillDevelopmentTypeToMatchAllProperties(updatedSkillDevelopmentType);
    }

    @Test
    @Transactional
    void putNonExistingSkillDevelopmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillDevelopmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDevelopmentType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillDevelopmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkillDevelopmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillDevelopmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkillDevelopmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDevelopmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillDevelopmentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentType = skillDevelopmentTypeRepository.saveAndFlush(skillDevelopmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillDevelopmentType using partial update
        SkillDevelopmentType partialUpdatedSkillDevelopmentType = new SkillDevelopmentType();
        partialUpdatedSkillDevelopmentType.setId(skillDevelopmentType.getId());

        restSkillDevelopmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillDevelopmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillDevelopmentType))
            )
            .andExpect(status().isOk());

        // Validate the SkillDevelopmentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillDevelopmentTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSkillDevelopmentType, skillDevelopmentType),
            getPersistedSkillDevelopmentType(skillDevelopmentType)
        );
    }

    @Test
    @Transactional
    void fullUpdateSkillDevelopmentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentType = skillDevelopmentTypeRepository.saveAndFlush(skillDevelopmentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillDevelopmentType using partial update
        SkillDevelopmentType partialUpdatedSkillDevelopmentType = new SkillDevelopmentType();
        partialUpdatedSkillDevelopmentType.setId(skillDevelopmentType.getId());

        partialUpdatedSkillDevelopmentType.skilldevelopmentname(UPDATED_SKILLDEVELOPMENTNAME);

        restSkillDevelopmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillDevelopmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillDevelopmentType))
            )
            .andExpect(status().isOk());

        // Validate the SkillDevelopmentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillDevelopmentTypeUpdatableFieldsEquals(
            partialUpdatedSkillDevelopmentType,
            getPersistedSkillDevelopmentType(partialUpdatedSkillDevelopmentType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSkillDevelopmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillDevelopmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillDevelopmentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDevelopmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkillDevelopmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDevelopmentType))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkillDevelopmentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(skillDevelopmentType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillDevelopmentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkillDevelopmentType() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentType = skillDevelopmentTypeRepository.saveAndFlush(skillDevelopmentType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skillDevelopmentType
        restSkillDevelopmentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, skillDevelopmentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillDevelopmentTypeRepository.count();
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

    protected SkillDevelopmentType getPersistedSkillDevelopmentType(SkillDevelopmentType skillDevelopmentType) {
        return skillDevelopmentTypeRepository.findById(skillDevelopmentType.getId()).orElseThrow();
    }

    protected void assertPersistedSkillDevelopmentTypeToMatchAllProperties(SkillDevelopmentType expectedSkillDevelopmentType) {
        assertSkillDevelopmentTypeAllPropertiesEquals(
            expectedSkillDevelopmentType,
            getPersistedSkillDevelopmentType(expectedSkillDevelopmentType)
        );
    }

    protected void assertPersistedSkillDevelopmentTypeToMatchUpdatableProperties(SkillDevelopmentType expectedSkillDevelopmentType) {
        assertSkillDevelopmentTypeAllUpdatablePropertiesEquals(
            expectedSkillDevelopmentType,
            getPersistedSkillDevelopmentType(expectedSkillDevelopmentType)
        );
    }
}
