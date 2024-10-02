package com.bracits.epms.service.impl;

import com.bracits.epms.domain.SkillDevelopmentDetails;
import com.bracits.epms.repository.SkillDevelopmentDetailsRepository;
import com.bracits.epms.service.SkillDevelopmentDetailsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.SkillDevelopmentDetails}.
 */
@Service
@Transactional
public class SkillDevelopmentDetailsServiceImpl implements SkillDevelopmentDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SkillDevelopmentDetailsServiceImpl.class);

    private final SkillDevelopmentDetailsRepository skillDevelopmentDetailsRepository;

    public SkillDevelopmentDetailsServiceImpl(SkillDevelopmentDetailsRepository skillDevelopmentDetailsRepository) {
        this.skillDevelopmentDetailsRepository = skillDevelopmentDetailsRepository;
    }

    @Override
    public SkillDevelopmentDetails save(SkillDevelopmentDetails skillDevelopmentDetails) {
        log.debug("Request to save SkillDevelopmentDetails : {}", skillDevelopmentDetails);
        return skillDevelopmentDetailsRepository.save(skillDevelopmentDetails);
    }

    @Override
    public SkillDevelopmentDetails update(SkillDevelopmentDetails skillDevelopmentDetails) {
        log.debug("Request to update SkillDevelopmentDetails : {}", skillDevelopmentDetails);
        return skillDevelopmentDetailsRepository.save(skillDevelopmentDetails);
    }

    @Override
    public Optional<SkillDevelopmentDetails> partialUpdate(SkillDevelopmentDetails skillDevelopmentDetails) {
        log.debug("Request to partially update SkillDevelopmentDetails : {}", skillDevelopmentDetails);

        return skillDevelopmentDetailsRepository.findById(skillDevelopmentDetails.getId()).map(skillDevelopmentDetailsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDevelopmentDetails> findAll() {
        log.debug("Request to get all SkillDevelopmentDetails");
        return skillDevelopmentDetailsRepository.findAll();
    }

    public Page<SkillDevelopmentDetails> findAllWithEagerRelationships(Pageable pageable) {
        return skillDevelopmentDetailsRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SkillDevelopmentDetails> findOne(Long id) {
        log.debug("Request to get SkillDevelopmentDetails : {}", id);
        return skillDevelopmentDetailsRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SkillDevelopmentDetails : {}", id);
        skillDevelopmentDetailsRepository.deleteById(id);
    }
}
