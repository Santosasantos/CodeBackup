package com.bracits.epms.service.impl;

import com.bracits.epms.domain.FeedbackDetails;
import com.bracits.epms.repository.FeedbackDetailsRepository;
import com.bracits.epms.service.FeedbackDetailsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.FeedbackDetails}.
 */
@Service
@Transactional
public class FeedbackDetailsServiceImpl implements FeedbackDetailsService {

    private static final Logger log = LoggerFactory.getLogger(FeedbackDetailsServiceImpl.class);

    private final FeedbackDetailsRepository feedbackDetailsRepository;

    public FeedbackDetailsServiceImpl(FeedbackDetailsRepository feedbackDetailsRepository) {
        this.feedbackDetailsRepository = feedbackDetailsRepository;
    }

    @Override
    public FeedbackDetails save(FeedbackDetails feedbackDetails) {
        log.debug("Request to save FeedbackDetails : {}", feedbackDetails);
        return feedbackDetailsRepository.save(feedbackDetails);
    }

    @Override
    public FeedbackDetails update(FeedbackDetails feedbackDetails) {
        log.debug("Request to update FeedbackDetails : {}", feedbackDetails);
        return feedbackDetailsRepository.save(feedbackDetails);
    }

    @Override
    public Optional<FeedbackDetails> partialUpdate(FeedbackDetails feedbackDetails) {
        log.debug("Request to partially update FeedbackDetails : {}", feedbackDetails);

        return feedbackDetailsRepository
            .findById(feedbackDetails.getId())
            .map(existingFeedbackDetails -> {
                if (feedbackDetails.getCommentsforfeedbacksubtype() != null) {
                    existingFeedbackDetails.setCommentsforfeedbacksubtype(feedbackDetails.getCommentsforfeedbacksubtype());
                }
                if (feedbackDetails.getRatingvalue() != null) {
                    existingFeedbackDetails.setRatingvalue(feedbackDetails.getRatingvalue());
                }

                return existingFeedbackDetails;
            })
            .map(feedbackDetailsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackDetails> findAll() {
        log.debug("Request to get all FeedbackDetails");
        return feedbackDetailsRepository.findAll();
    }

    public Page<FeedbackDetails> findAllWithEagerRelationships(Pageable pageable) {
        return feedbackDetailsRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    public Optional<Double> findAverageRatingfromOther(String pin, String feedbacksubname, Integer year) {
        return feedbackDetailsRepository.findAverageRatingforOther(pin, feedbacksubname, year);
    }

    @Override
    public Optional<Integer> findSelfRating(String pin, String feedbacksubname, Integer year) {
        return feedbackDetailsRepository.findRatingforSelf(pin, feedbacksubname, year);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FeedbackDetails> findOne(Long id) {
        log.debug("Request to get FeedbackDetails : {}", id);
        return feedbackDetailsRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FeedbackDetails : {}", id);
        feedbackDetailsRepository.deleteById(id);
    }
}
