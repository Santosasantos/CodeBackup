package com.bracits.epms.service;

import com.bracits.epms.domain.Extraquestion;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.Extraquestion}.
 */
public interface ExtraquestionService {
    /**
     * Save a extraquestion.
     *
     * @param extraquestion the entity to save.
     * @return the persisted entity.
     */
    Extraquestion save(Extraquestion extraquestion);

    /**
     * Updates a extraquestion.
     *
     * @param extraquestion the entity to update.
     * @return the persisted entity.
     */
    Extraquestion update(Extraquestion extraquestion);

    /**
     * Partially updates a extraquestion.
     *
     * @param extraquestion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Extraquestion> partialUpdate(Extraquestion extraquestion);

    /**
     * Get all the extraquestions.
     *
     * @return the list of entities.
     */
    List<Extraquestion> findAll();

    List<Extraquestion> findByFeedbackId(Long feedbackId);

    /**
     * Get the "id" extraquestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Extraquestion> findOne(Long id);

    /**
     * Delete the "id" extraquestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
