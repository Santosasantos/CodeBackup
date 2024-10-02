package com.bracits.epms.service;

import com.bracits.epms.domain.FeedbackResponder;
import com.bracits.epms.domain.enumeration.FeedbackStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.FeedbackResponder}.
 */
public interface FeedbackResponderService {
    /**
     * Save a feedbackResponder.
     *
     * @param feedbackResponder the entity to save.
     * @return the persisted entity.
     */
    FeedbackResponder save(FeedbackResponder feedbackResponder);

    /**
     * Updates a feedbackResponder.
     *
     * @param feedbackResponder the entity to update.
     * @return the persisted entity.
     */
    FeedbackResponder update(FeedbackResponder feedbackResponder);

    /**
     * Partially updates a feedbackResponder.
     *
     * @param feedbackResponder the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FeedbackResponder> partialUpdate(FeedbackResponder feedbackResponder);

    /**
     * Get all the feedbackResponders.
     *
     * @return the list of entities.
     */
    List<FeedbackResponder> findAll();

    /**
     * Get all the feedbackResponders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FeedbackResponder> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" feedbackResponder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FeedbackResponder> findOne(Long id);
    FeedbackResponder updateResponderStatus(Long id, FeedbackStatus responderstatus);

    List<FeedbackResponder> getFeedbackRespondersWithFeedback(Long feedbackid);

    List<FeedbackResponder> findAllRequester(String pin, Integer year);
    /**
     * Delete the "id" feedbackResponder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
