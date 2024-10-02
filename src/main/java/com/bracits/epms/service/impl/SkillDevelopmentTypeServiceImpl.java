package com.bracits.epms.service.impl;

import com.bracits.epms.domain.SkillDevelopmentType;
import com.bracits.epms.repository.SkillDevelopmentTypeRepository;
import com.bracits.epms.service.SkillDevelopmentTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.SkillDevelopmentType}.
 */
@Service
@Transactional
public class SkillDevelopmentTypeServiceImpl implements SkillDevelopmentTypeService {

    private static final Logger log = LoggerFactory.getLogger(SkillDevelopmentTypeServiceImpl.class);

    private final SkillDevelopmentTypeRepository skillDevelopmentTypeRepository;

    public SkillDevelopmentTypeServiceImpl(SkillDevelopmentTypeRepository skillDevelopmentTypeRepository) {
        this.skillDevelopmentTypeRepository = skillDevelopmentTypeRepository;
    }

    @Override
    public SkillDevelopmentType save(SkillDevelopmentType skillDevelopmentType) {
        log.debug("Request to save SkillDevelopmentType : {}", skillDevelopmentType);
        return skillDevelopmentTypeRepository.save(skillDevelopmentType);
    }

    @Override
    public SkillDevelopmentType update(SkillDevelopmentType skillDevelopmentType) {
        log.debug("Request to update SkillDevelopmentType : {}", skillDevelopmentType);
        return skillDevelopmentTypeRepository.save(skillDevelopmentType);
    }

    @Override
    public Optional<SkillDevelopmentType> partialUpdate(SkillDevelopmentType skillDevelopmentType) {
        log.debug("Request to partially update SkillDevelopmentType : {}", skillDevelopmentType);

        return skillDevelopmentTypeRepository
            .findById(skillDevelopmentType.getId())
            .map(existingSkillDevelopmentType -> {
                if (skillDevelopmentType.getSkilldevelopmentname() != null) {
                    existingSkillDevelopmentType.setSkilldevelopmentname(skillDevelopmentType.getSkilldevelopmentname());
                }

                return existingSkillDevelopmentType;
            })
            .map(skillDevelopmentTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDevelopmentType> findAll() {
        log.debug("Request to get all SkillDevelopmentTypes");
        return skillDevelopmentTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SkillDevelopmentType> findOne(Long id) {
        log.debug("Request to get SkillDevelopmentType : {}", id);
        return skillDevelopmentTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SkillDevelopmentType : {}", id);
        skillDevelopmentTypeRepository.deleteById(id);
    }
}
