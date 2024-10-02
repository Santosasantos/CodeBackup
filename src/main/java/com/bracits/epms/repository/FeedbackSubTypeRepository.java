package com.bracits.epms.repository;

import com.bracits.epms.domain.FeedbackSubType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeedbackSubType entity.
 */
@Repository
public interface FeedbackSubTypeRepository extends JpaRepository<FeedbackSubType, Long> {
    default Optional<FeedbackSubType> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FeedbackSubType> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FeedbackSubType> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select feedbackSubType from FeedbackSubType feedbackSubType left join fetch feedbackSubType.feedbackType",
        countQuery = "select count(feedbackSubType) from FeedbackSubType feedbackSubType"
    )
    Page<FeedbackSubType> findAllWithToOneRelationships(Pageable pageable);

    @Query("select feedbackSubType from FeedbackSubType feedbackSubType left join fetch feedbackSubType.feedbackType")
    List<FeedbackSubType> findAllWithToOneRelationships();

    @Query(
        "select feedbackSubType from FeedbackSubType feedbackSubType left join fetch feedbackSubType.feedbackType where feedbackSubType.id =:id"
    )
    Optional<FeedbackSubType> findOneWithToOneRelationships(@Param("id") Long id);
}
