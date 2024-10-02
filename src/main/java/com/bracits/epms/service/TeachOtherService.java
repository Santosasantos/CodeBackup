package com.bracits.epms.service;

import com.bracits.epms.domain.TeachOther;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.bracits.epms.domain.TeachOther}.
 */
public interface TeachOtherService {
    /**
     * Save a teachOther.
     *
     * @param teachOther the entity to save.
     * @return the persisted entity.
     */
    TeachOther save(TeachOther teachOther);

    /**
     * Updates a teachOther.
     *
     * @param teachOther the entity to update.
     * @return the persisted entity.
     */
    TeachOther update(TeachOther teachOther);

    /**
     * Partially updates a teachOther.
     *
     * @param teachOther the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TeachOther> partialUpdate(TeachOther teachOther);

    /**
     * Get all the teachOthers.
     *
     * @return the list of entities.
     */
    List<TeachOther> findAll();

    /**
     * Get the "id" teachOther.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeachOther> findOne(Long id);

    /**
     * Delete the "id" teachOther.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
