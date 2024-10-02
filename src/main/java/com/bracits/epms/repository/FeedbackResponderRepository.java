package com.bracits.epms.repository;

import com.bracits.epms.domain.FeedbackResponder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FeedbackResponder entity.
 */
@Repository
public interface FeedbackResponderRepository extends JpaRepository<FeedbackResponder, Long> {
    default Optional<FeedbackResponder> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FeedbackResponder> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FeedbackResponder> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select feedbackResponder from FeedbackResponder feedbackResponder left join fetch feedbackResponder.employee",
        countQuery = "select count(feedbackResponder) from FeedbackResponder feedbackResponder"
    )
    Page<FeedbackResponder> findAllWithToOneRelationships(Pageable pageable);

    @Query("select feedbackResponder from FeedbackResponder feedbackResponder left join fetch feedbackResponder.employee")
    List<FeedbackResponder> findAllWithToOneRelationships();

    @Query(
        "select feedbackResponder from FeedbackResponder feedbackResponder left join fetch feedbackResponder.employee where feedbackResponder.id =:id"
    )
    Optional<FeedbackResponder> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select feedbackResponder from FeedbackResponder feedbackResponder left join fetch feedbackResponder.feedback and left join fetch feedbackResponder.employee where feedbackResponder.feedback.id =:id"
    )
    List<FeedbackResponder> getFeedbackRespondersWithFeedback(@Param("id") Long id);

    @Query(
        "select fr from FeedbackResponder fr right join fetch fr.feedback and left join fetch fr.employee where fr.employee.pin =:pin and fr.feedback.assessmentYear =:year"
    )
    List<FeedbackResponder> findAllRequester(@Param("pin") String pin, @Param("year") Integer year);
}
