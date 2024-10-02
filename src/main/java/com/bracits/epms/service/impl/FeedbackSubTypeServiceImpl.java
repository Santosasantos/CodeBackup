package com.bracits.epms.service.impl;

import com.bracits.epms.domain.FeedbackSubType;
import com.bracits.epms.repository.FeedbackSubTypeRepository;
import com.bracits.epms.service.FeedbackSubTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.FeedbackSubType}.
 */
@Service
@Transactional
public class FeedbackSubTypeServiceImpl implements FeedbackSubTypeService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackSubTypeServiceImpl.class);

    private final FeedbackSubTypeRepository feedbackSubTypeRepository;

    public FeedbackSubTypeServiceImpl(FeedbackSubTypeRepository feedbackSubTypeRepository) {
        this.feedbackSubTypeRepository = feedbackSubTypeRepository;
    }

    @Override
    public FeedbackSubType save(FeedbackSubType feedbackSubType) {
        log.debug("Request to save FeedbackSubType : {}", feedbackSubType);
        return feedbackSubTypeRepository.save(feedbackSubType);
    }

    @Override
    public FeedbackSubType update(FeedbackSubType feedbackSubType) {
        log.debug("Request to update FeedbackSubType : {}", feedbackSubType);
        return feedbackSubTypeRepository.save(feedbackSubType);
    }

    @Override
    public Optional<FeedbackSubType> partialUpdate(FeedbackSubType feedbackSubType) {
        log.debug("Request to partially update FeedbackSubType : {}", feedbackSubType);

        return feedbackSubTypeRepository
            .findById(feedbackSubType.getId())
            .map(existingFeedbackSubType -> {
                if (feedbackSubType.getFeedbacksubname() != null) {
                    existingFeedbackSubType.setFeedbacksubname(feedbackSubType.getFeedbacksubname());
                }
                if (feedbackSubType.getFeedbackdescription() != null) {
                    existingFeedbackSubType.setFeedbackdescription(feedbackSubType.getFeedbackdescription());
                }

                return existingFeedbackSubType;
            })
            .map(feedbackSubTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackSubType> findAll() {
        log.debug("Request to get all FeedbackSubTypes");
        return feedbackSubTypeRepository.findAll();
    }

    public Page<FeedbackSubType> findAllWithEagerRelationships(Pageable pageable) {
        return feedbackSubTypeRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackSubType> findOne(Long id) {
        log.debug("Request to get FeedbackSubType : {}", id);
        return feedbackSubTypeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FeedbackSubType : {}", id);
        feedbackSubTypeRepository.deleteById(id);
    }
}
