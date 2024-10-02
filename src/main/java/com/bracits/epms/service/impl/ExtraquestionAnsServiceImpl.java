package com.bracits.epms.service.impl;

import com.bracits.epms.domain.ExtraquestionAns;
import com.bracits.epms.repository.ExtraquestionAnsRepository;
import com.bracits.epms.service.ExtraquestionAnsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.ExtraquestionAns}.
 */
@Service
@Transactional
public class ExtraquestionAnsServiceImpl implements ExtraquestionAnsService {

    private static final Logger log = LoggerFactory.getLogger(ExtraquestionAnsServiceImpl.class);

    private final ExtraquestionAnsRepository extraquestionAnsRepository;

    public ExtraquestionAnsServiceImpl(ExtraquestionAnsRepository extraquestionAnsRepository) {
        this.extraquestionAnsRepository = extraquestionAnsRepository;
    }

    @Override
    public ExtraquestionAns save(ExtraquestionAns extraquestionAns) {
        log.debug("Request to save ExtraquestionAns : {}", extraquestionAns);
        return extraquestionAnsRepository.save(extraquestionAns);
    }

    @Override
    public ExtraquestionAns update(ExtraquestionAns extraquestionAns) {
        log.debug("Request to update ExtraquestionAns : {}", extraquestionAns);
        return extraquestionAnsRepository.save(extraquestionAns);
    }

    @Override
    public Optional<ExtraquestionAns> partialUpdate(ExtraquestionAns extraquestionAns) {
        log.debug("Request to partially update ExtraquestionAns : {}", extraquestionAns);

        return extraquestionAnsRepository
            .findById(extraquestionAns.getId())
            .map(existingExtraquestionAns -> {
                if (extraquestionAns.getQuestionans() != null) {
                    existingExtraquestionAns.setQuestionans(extraquestionAns.getQuestionans());
                }

                return existingExtraquestionAns;
            })
            .map(extraquestionAnsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExtraquestionAns> findAll() {
        log.debug("Request to get all ExtraquestionAns");
        return extraquestionAnsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExtraquestionAns> findOne(Long id) {
        log.debug("Request to get ExtraquestionAns : {}", id);
        return extraquestionAnsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExtraquestionAns : {}", id);
        extraquestionAnsRepository.deleteById(id);
    }
}
