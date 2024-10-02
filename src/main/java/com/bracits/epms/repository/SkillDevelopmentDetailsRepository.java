package com.bracits.epms.repository;

import com.bracits.epms.domain.SkillDevelopmentDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SkillDevelopmentDetails entity.
 */
@Repository
public interface SkillDevelopmentDetailsRepository extends JpaRepository<SkillDevelopmentDetails, Long> {
    default Optional<SkillDevelopmentDetails> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SkillDevelopmentDetails> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SkillDevelopmentDetails> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select skillDevelopmentDetails from SkillDevelopmentDetails skillDevelopmentDetails left join fetch skillDevelopmentDetails.skillDevelopmentType",
        countQuery = "select count(skillDevelopmentDetails) from SkillDevelopmentDetails skillDevelopmentDetails"
    )
    Page<SkillDevelopmentDetails> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select skillDevelopmentDetails from SkillDevelopmentDetails skillDevelopmentDetails left join fetch skillDevelopmentDetails.skillDevelopmentType"
    )
    List<SkillDevelopmentDetails> findAllWithToOneRelationships();

    @Query(
        "select skillDevelopmentDetails from SkillDevelopmentDetails skillDevelopmentDetails left join fetch skillDevelopmentDetails.skillDevelopmentType where skillDevelopmentDetails.id =:id"
    )
    Optional<SkillDevelopmentDetails> findOneWithToOneRelationships(@Param("id") Long id);
}
