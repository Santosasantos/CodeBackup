package com.bracits.epms.service;

import com.bracits.epms.domain.ExtraquestionAns;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.ExtraquestionAns}.
 */
public interface ExtraquestionAnsService {
    /**
     * Save a extraquestionAns.
     *
     * @param extraquestionAns the entity to save.
     * @return the persisted entity.
     */
    ExtraquestionAns save(ExtraquestionAns extraquestionAns);

    /**
     * Updates a extraquestionAns.
     *
     * @param extraquestionAns the entity to update.
     * @return the persisted entity.
     */
    ExtraquestionAns update(ExtraquestionAns extraquestionAns);

    /**
     * Partially updates a extraquestionAns.
     *
     * @param extraquestionAns the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExtraquestionAns> partialUpdate(ExtraquestionAns extraquestionAns);

    /**
     * Get all the extraquestionAns.
     *
     * @return the list of entities.
     */
    List<ExtraquestionAns> findAll();

    /**
     * Get the "id" extraquestionAns.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExtraquestionAns> findOne(Long id);

    /**
     * Delete the "id" extraquestionAns.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
