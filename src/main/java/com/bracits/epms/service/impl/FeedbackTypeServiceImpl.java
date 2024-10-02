package com.bracits.epms.service.impl;

import com.bracits.epms.domain.FeedbackType;
import com.bracits.epms.repository.FeedbackTypeRepository;
import com.bracits.epms.service.FeedbackTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.FeedbackType}.
 */
@Service
@Transactional
public class FeedbackTypeServiceImpl implements FeedbackTypeService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackTypeServiceImpl.class);

    private final FeedbackTypeRepository feedbackTypeRepository;

    public FeedbackTypeServiceImpl(FeedbackTypeRepository feedbackTypeRepository) {
        this.feedbackTypeRepository = feedbackTypeRepository;
    }

    @Override
    public FeedbackType save(FeedbackType feedbackType) {
        log.debug("Request to save FeedbackType : {}", feedbackType);
        return feedbackTypeRepository.save(feedbackType);
    }

    @Override
    public FeedbackType update(FeedbackType feedbackType) {
        log.debug("Request to update FeedbackType : {}", feedbackType);
        return feedbackTypeRepository.save(feedbackType);
    }

    @Override
    public Optional<FeedbackType> partialUpdate(FeedbackType feedbackType) {
        log.debug("Request to partially update FeedbackType : {}", feedbackType);

        return feedbackTypeRepository
            .findById(feedbackType.getId())
            .map(existingFeedbackType -> {
                if (feedbackType.getFeedbackname() != null) {
                    existingFeedbackType.setFeedbackname(feedbackType.getFeedbackname());
                }

                return existingFeedbackType;
            })
            .map(feedbackTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackType> findAll() {
        log.debug("Request to get all FeedbackTypes");
        return feedbackTypeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackType> findOne(Long id) {
        log.debug("Request to get FeedbackType : {}", id);
        return feedbackTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FeedbackType : {}", id);
        feedbackTypeRepository.deleteById(id);
    }
}
