package com.bracits.epms.service.impl;

import com.bracits.epms.domain.TeachOther;
import com.bracits.epms.repository.TeachOtherRepository;
import com.bracits.epms.service.TeachOtherService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.TeachOther}.
 */
@Service
@Transactional
public class TeachOtherServiceImpl implements TeachOtherService {

    private static final Logger log = LoggerFactory.getLogger(TeachOtherServiceImpl.class);

    private final TeachOtherRepository teachOtherRepository;

    public TeachOtherServiceImpl(TeachOtherRepository teachOtherRepository) {
        this.teachOtherRepository = teachOtherRepository;
    }

    @Override
    public TeachOther save(TeachOther teachOther) {
        log.debug("Request to save TeachOther : {}", teachOther);
        return teachOtherRepository.save(teachOther);
    }

    @Override
    public TeachOther update(TeachOther teachOther) {
        log.debug("Request to update TeachOther : {}", teachOther);
        return teachOtherRepository.save(teachOther);
    }

    @Override
    public Optional<TeachOther> partialUpdate(TeachOther teachOther) {
        log.debug("Request to partially update TeachOther : {}", teachOther);

        return teachOtherRepository
            .findById(teachOther.getId())
            .map(existingTeachOther -> {
                if (teachOther.getTechnicalSkill() != null) {
                    existingTeachOther.setTechnicalSkill(teachOther.getTechnicalSkill());
                }
                if (teachOther.getRecommendation() != null) {
                    existingTeachOther.setRecommendation(teachOther.getRecommendation());
                }
                if (teachOther.getParticularStrengh() != null) {
                    existingTeachOther.setParticularStrengh(teachOther.getParticularStrengh());
                }
                if (teachOther.getWhynotRecommend() != null) {
                    existingTeachOther.setWhynotRecommend(teachOther.getWhynotRecommend());
                }

                return existingTeachOther;
            })
            .map(teachOtherRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeachOther> findAll() {
        log.debug("Request to get all TeachOthers");
        return teachOtherRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TeachOther> findOne(Long id) {
        log.debug("Request to get TeachOther : {}", id);
        return teachOtherRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TeachOther : {}", id);
        teachOtherRepository.deleteById(id);
    }
}
