package com.bracits.epms.service;

import com.bracits.epms.domain.RatingScale;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.RatingScale}.
 */
public interface RatingScaleService {
    /**
     * Save a ratingScale.
     *
     * @param ratingScale the entity to save.
     * @return the persisted entity.
     */
    RatingScale save(RatingScale ratingScale);

    /**
     * Updates a ratingScale.
     *
     * @param ratingScale the entity to update.
     * @return the persisted entity.
     */
    RatingScale update(RatingScale ratingScale);

    /**
     * Partially updates a ratingScale.
     *
     * @param ratingScale the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RatingScale> partialUpdate(RatingScale ratingScale);

    /**
     * Get all the ratingScales.
     *
     * @return the list of entities.
     */
    List<RatingScale> findAll();

    /**
     * Get the "id" ratingScale.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RatingScale> findOne(Long id);

    /**
     * Delete the "id" ratingScale.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
