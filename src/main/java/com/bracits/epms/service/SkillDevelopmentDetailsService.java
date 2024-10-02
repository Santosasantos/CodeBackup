package com.bracits.epms.service;

import com.bracits.epms.domain.SkillDevelopmentDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.SkillDevelopmentDetails}.
 */
public interface SkillDevelopmentDetailsService {
    /**
     * Save a skillDevelopmentDetails.
     *
     * @param skillDevelopmentDetails the entity to save.
     * @return the persisted entity.
     */
    SkillDevelopmentDetails save(SkillDevelopmentDetails skillDevelopmentDetails);

    /**
     * Updates a skillDevelopmentDetails.
     *
     * @param skillDevelopmentDetails the entity to update.
     * @return the persisted entity.
     */
    SkillDevelopmentDetails update(SkillDevelopmentDetails skillDevelopmentDetails);

    /**
     * Partially updates a skillDevelopmentDetails.
     *
     * @param skillDevelopmentDetails the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SkillDevelopmentDetails> partialUpdate(SkillDevelopmentDetails skillDevelopmentDetails);

    /**
     * Get all the skillDevelopmentDetails.
     *
     * @return the list of entities.
     */
    List<SkillDevelopmentDetails> findAll();

    /**
     * Get all the skillDevelopmentDetails with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SkillDevelopmentDetails> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" skillDevelopmentDetails.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SkillDevelopmentDetails> findOne(Long id);

    /**
     * Delete the "id" skillDevelopmentDetails.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
