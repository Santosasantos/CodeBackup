package com.bracits.epms.web.rest;

import static com.bracits.epms.domain.SkillDevelopmentDetailsAsserts.*;
import static com.bracits.epms.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bracits.epms.IntegrationTest;
import com.bracits.epms.domain.SkillDevelopmentDetails;
import com.bracits.epms.repository.SkillDevelopmentDetailsRepository;
import com.bracits.epms.service.SkillDevelopmentDetailsService;
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
 * Integration tests for the {@link SkillDevelopmentDetailsResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SkillDevelopmentDetailsResourceIT {

    private static final String ENTITY_API_URL = "/api/skill-development-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SkillDevelopmentDetailsRepository skillDevelopmentDetailsRepository;

    @Mock
    private SkillDevelopmentDetailsRepository skillDevelopmentDetailsRepositoryMock;

    @Mock
    private SkillDevelopmentDetailsService skillDevelopmentDetailsServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSkillDevelopmentDetailsMockMvc;

    private SkillDevelopmentDetails skillDevelopmentDetails;

    private SkillDevelopmentDetails insertedSkillDevelopmentDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillDevelopmentDetails createEntity(EntityManager em) {
        SkillDevelopmentDetails skillDevelopmentDetails = new SkillDevelopmentDetails();
        return skillDevelopmentDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillDevelopmentDetails createUpdatedEntity(EntityManager em) {
        SkillDevelopmentDetails skillDevelopmentDetails = new SkillDevelopmentDetails();
        return skillDevelopmentDetails;
    }

    @BeforeEach
    public void initTest() {
        skillDevelopmentDetails = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedSkillDevelopmentDetails != null) {
            skillDevelopmentDetailsRepository.delete(insertedSkillDevelopmentDetails);
            insertedSkillDevelopmentDetails = null;
        }
    }

    @Test
    @Transactional
    void createSkillDevelopmentDetails() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SkillDevelopmentDetails
        var returnedSkillDevelopmentDetails = om.readValue(
            restSkillDevelopmentDetailsMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDevelopmentDetails))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SkillDevelopmentDetails.class
        );

        // Validate the SkillDevelopmentDetails in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSkillDevelopmentDetailsUpdatableFieldsEquals(
            returnedSkillDevelopmentDetails,
            getPersistedSkillDevelopmentDetails(returnedSkillDevelopmentDetails)
        );

        insertedSkillDevelopmentDetails = returnedSkillDevelopmentDetails;
    }

    @Test
    @Transactional
    void createSkillDevelopmentDetailsWithExistingId() throws Exception {
        // Create the SkillDevelopmentDetails with an existing ID
        skillDevelopmentDetails.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillDevelopmentDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDevelopmentDetails)))
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSkillDevelopmentDetails() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentDetails = skillDevelopmentDetailsRepository.saveAndFlush(skillDevelopmentDetails);

        // Get all the skillDevelopmentDetailsList
        restSkillDevelopmentDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillDevelopmentDetails.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSkillDevelopmentDetailsWithEagerRelationshipsIsEnabled() throws Exception {
        when(skillDevelopmentDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSkillDevelopmentDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(skillDevelopmentDetailsServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSkillDevelopmentDetailsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(skillDevelopmentDetailsServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSkillDevelopmentDetailsMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(skillDevelopmentDetailsRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSkillDevelopmentDetails() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentDetails = skillDevelopmentDetailsRepository.saveAndFlush(skillDevelopmentDetails);

        // Get the skillDevelopmentDetails
        restSkillDevelopmentDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, skillDevelopmentDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(skillDevelopmentDetails.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSkillDevelopmentDetails() throws Exception {
        // Get the skillDevelopmentDetails
        restSkillDevelopmentDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSkillDevelopmentDetails() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentDetails = skillDevelopmentDetailsRepository.saveAndFlush(skillDevelopmentDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillDevelopmentDetails
        SkillDevelopmentDetails updatedSkillDevelopmentDetails = skillDevelopmentDetailsRepository
            .findById(skillDevelopmentDetails.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSkillDevelopmentDetails are not directly saved in db
        em.detach(updatedSkillDevelopmentDetails);

        restSkillDevelopmentDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSkillDevelopmentDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSkillDevelopmentDetails))
            )
            .andExpect(status().isOk());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSkillDevelopmentDetailsToMatchAllProperties(updatedSkillDevelopmentDetails);
    }

    @Test
    @Transactional
    void putNonExistingSkillDevelopmentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillDevelopmentDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, skillDevelopmentDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillDevelopmentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSkillDevelopmentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(skillDevelopmentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSkillDevelopmentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(skillDevelopmentDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSkillDevelopmentDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentDetails = skillDevelopmentDetailsRepository.saveAndFlush(skillDevelopmentDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillDevelopmentDetails using partial update
        SkillDevelopmentDetails partialUpdatedSkillDevelopmentDetails = new SkillDevelopmentDetails();
        partialUpdatedSkillDevelopmentDetails.setId(skillDevelopmentDetails.getId());

        restSkillDevelopmentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillDevelopmentDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillDevelopmentDetails))
            )
            .andExpect(status().isOk());

        // Validate the SkillDevelopmentDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillDevelopmentDetailsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSkillDevelopmentDetails, skillDevelopmentDetails),
            getPersistedSkillDevelopmentDetails(skillDevelopmentDetails)
        );
    }

    @Test
    @Transactional
    void fullUpdateSkillDevelopmentDetailsWithPatch() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentDetails = skillDevelopmentDetailsRepository.saveAndFlush(skillDevelopmentDetails);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the skillDevelopmentDetails using partial update
        SkillDevelopmentDetails partialUpdatedSkillDevelopmentDetails = new SkillDevelopmentDetails();
        partialUpdatedSkillDevelopmentDetails.setId(skillDevelopmentDetails.getId());

        restSkillDevelopmentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSkillDevelopmentDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSkillDevelopmentDetails))
            )
            .andExpect(status().isOk());

        // Validate the SkillDevelopmentDetails in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSkillDevelopmentDetailsUpdatableFieldsEquals(
            partialUpdatedSkillDevelopmentDetails,
            getPersistedSkillDevelopmentDetails(partialUpdatedSkillDevelopmentDetails)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSkillDevelopmentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentDetails.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillDevelopmentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, skillDevelopmentDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDevelopmentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSkillDevelopmentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(skillDevelopmentDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSkillDevelopmentDetails() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        skillDevelopmentDetails.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSkillDevelopmentDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(skillDevelopmentDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SkillDevelopmentDetails in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSkillDevelopmentDetails() throws Exception {
        // Initialize the database
        insertedSkillDevelopmentDetails = skillDevelopmentDetailsRepository.saveAndFlush(skillDevelopmentDetails);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the skillDevelopmentDetails
        restSkillDevelopmentDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, skillDevelopmentDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return skillDevelopmentDetailsRepository.count();
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

    protected SkillDevelopmentDetails getPersistedSkillDevelopmentDetails(SkillDevelopmentDetails skillDevelopmentDetails) {
        return skillDevelopmentDetailsRepository.findById(skillDevelopmentDetails.getId()).orElseThrow();
    }

    protected void assertPersistedSkillDevelopmentDetailsToMatchAllProperties(SkillDevelopmentDetails expectedSkillDevelopmentDetails) {
        assertSkillDevelopmentDetailsAllPropertiesEquals(
            expectedSkillDevelopmentDetails,
            getPersistedSkillDevelopmentDetails(expectedSkillDevelopmentDetails)
        );
    }

    protected void assertPersistedSkillDevelopmentDetailsToMatchUpdatableProperties(
        SkillDevelopmentDetails expectedSkillDevelopmentDetails
    ) {
        assertSkillDevelopmentDetailsAllUpdatablePropertiesEquals(
            expectedSkillDevelopmentDetails,
            getPersistedSkillDevelopmentDetails(expectedSkillDevelopmentDetails)
        );
    }
}
