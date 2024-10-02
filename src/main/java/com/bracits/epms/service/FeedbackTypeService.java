package com.bracits.epms.service;

import com.bracits.epms.domain.FeedbackType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.FeedbackType}.
 */
public interface FeedbackTypeService {
    /**
     * Save a feedbackType.
     *
     * @param feedbackType the entity to save.
     * @return the persisted entity.
     */
    FeedbackType save(FeedbackType feedbackType);

    /**
     * Updates a feedbackType.
     *
     * @param feedbackType the entity to update.
     * @return the persisted entity.
     */
    FeedbackType update(FeedbackType feedbackType);

    /**
     * Partially updates a feedbackType.
     *
     * @param feedbackType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeedbackType> partialUpdate(FeedbackType feedbackType);

    /**
     * Get all the feedbackTypes.
     *
     * @return the list of entities.
     */
    List<FeedbackType> findAll();

    /**
     * Get the "id" feedbackType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeedbackType> findOne(Long id);

    /**
     * Delete the "id" feedbackType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
