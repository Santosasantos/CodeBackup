package com.bracits.epms.service.impl;

import com.bracits.epms.domain.FeedbackResponder;
import com.bracits.epms.domain.enumeration.FeedbackStatus;
import com.bracits.epms.repository.FeedbackResponderRepository;
import com.bracits.epms.service.FeedbackResponderService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.FeedbackResponder}.
 */
@Service
@Transactional
public class FeedbackResponderServiceImpl implements FeedbackResponderService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackResponderServiceImpl.class);

    private final FeedbackResponderRepository feedbackResponderRepository;

    public FeedbackResponderServiceImpl(FeedbackResponderRepository feedbackResponderRepository) {
        this.feedbackResponderRepository = feedbackResponderRepository;
    }

    @Override
    public FeedbackResponder save(FeedbackResponder feedbackResponder) {
        log.debug("Request to save FeedbackResponder : {}", feedbackResponder);
        return feedbackResponderRepository.save(feedbackResponder);
    }

    @Override
    public FeedbackResponder update(FeedbackResponder feedbackResponder) {
        log.debug("Request to update FeedbackResponder : {}", feedbackResponder);
        return feedbackResponderRepository.save(feedbackResponder);
    }

    @Override
    public Optional<FeedbackResponder> partialUpdate(FeedbackResponder feedbackResponder) {
        log.debug("Request to partially update FeedbackResponder : {}", feedbackResponder);

        return feedbackResponderRepository
            .findById(feedbackResponder.getId())
            .map(existingFeedbackResponder -> {
                if (feedbackResponder.getCategory() != null) {
                    existingFeedbackResponder.setCategory(feedbackResponder.getCategory());
                }
                if (feedbackResponder.getStakeholderEmail() != null) {
                    existingFeedbackResponder.setStakeholderEmail(feedbackResponder.getStakeholderEmail());
                }
                if (feedbackResponder.getResponderStatus() != null) {
                    existingFeedbackResponder.setResponderStatus(feedbackResponder.getResponderStatus());
                }

                return existingFeedbackResponder;
            })
            .map(feedbackResponderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponder> findAll() {
        log.debug("Request to get all FeedbackResponders");
        return feedbackResponderRepository.findAll();
    }

    public Page<FeedbackResponder> findAllWithEagerRelationships(Pageable pageable) {
        return feedbackResponderRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackResponder> findOne(Long id) {
        log.debug("Request to get FeedbackResponder : {}", id);
        return feedbackResponderRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public FeedbackResponder updateResponderStatus(Long id, FeedbackStatus responderstatus) {
        log.debug("Request to update FeedbackResponder : {}", id);
        FeedbackResponder feedbackResponder = feedbackResponderRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("FeedbackResponder not found"));
        feedbackResponder.setResponderStatus(responderstatus);
        return feedbackResponderRepository.save(feedbackResponder);
    }

    @Override
    public List<FeedbackResponder> getFeedbackRespondersWithFeedback(Long feedbackid) {
        log.debug("Request to get FeedbackResponders with feedback : {}", feedbackid);
        return feedbackResponderRepository.getFeedbackRespondersWithFeedback(feedbackid);
    }

    @Override
    public List<FeedbackResponder> findAllRequester(String pin, Integer year) {
        log.debug("Request to get all FeedbackResponders with requester : {}", pin, year);
        return feedbackResponderRepository.findAllRequester(pin, year);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FeedbackResponder : {}", id);
        feedbackResponderRepository.deleteById(id);
    }
}
