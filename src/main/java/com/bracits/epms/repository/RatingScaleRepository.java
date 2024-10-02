package com.bracits.epms.repository;

import com.bracits.epms.domain.RatingScale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RatingScale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RatingScaleRepository extends JpaRepository<RatingScale, Long> {}
