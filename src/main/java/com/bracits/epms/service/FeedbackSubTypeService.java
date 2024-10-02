package com.bracits.epms.service;

import com.bracits.epms.domain.FeedbackSubType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.FeedbackSubType}.
 */
public interface FeedbackSubTypeService {
    /**
     * Save a feedbackSubType.
     *
     * @param feedbackSubType the entity to save.
     * @return the persisted entity.
     */
    FeedbackSubType save(FeedbackSubType feedbackSubType);

    /**
     * Updates a feedbackSubType.
     *
     * @param feedbackSubType the entity to update.
     * @return the persisted entity.
     */
    FeedbackSubType update(FeedbackSubType feedbackSubType);

    /**
     * Partially updates a feedbackSubType.
     *
     * @param feedbackSubType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeedbackSubType> partialUpdate(FeedbackSubType feedbackSubType);

    /**
     * Get all the feedbackSubTypes.
     *
     * @return the list of entities.
     */
    List<FeedbackSubType> findAll();

    /**
     * Get all the feedbackSubTypes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FeedbackSubType> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" feedbackSubType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeedbackSubType> findOne(Long id);

    /**
     * Delete the "id" feedbackSubType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
