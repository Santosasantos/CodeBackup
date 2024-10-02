package com.bracits.epms.service;

import com.bracits.epms.domain.SkillDevelopmentType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.SkillDevelopmentType}.
 */
public interface SkillDevelopmentTypeService {
    /**
     * Save a skillDevelopmentType.
     *
     * @param skillDevelopmentType the entity to save.
     * @return the persisted entity.
     */
    SkillDevelopmentType save(SkillDevelopmentType skillDevelopmentType);

    /**
     * Updates a skillDevelopmentType.
     *
     * @param skillDevelopmentType the entity to update.
     * @return the persisted entity.
     */
    SkillDevelopmentType update(SkillDevelopmentType skillDevelopmentType);

    /**
     * Partially updates a skillDevelopmentType.
     *
     * @param skillDevelopmentType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SkillDevelopmentType> partialUpdate(SkillDevelopmentType skillDevelopmentType);

    /**
     * Get all the skillDevelopmentTypes.
     *
     * @return the list of entities.
     */
    List<SkillDevelopmentType> findAll();

    /**
     * Get the "id" skillDevelopmentType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SkillDevelopmentType> findOne(Long id);

    /**
     * Delete the "id" skillDevelopmentType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
