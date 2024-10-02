package com.bracits.epms.service.impl;

import com.bracits.epms.domain.RatingScale;
import com.bracits.epms.repository.RatingScaleRepository;
import com.bracits.epms.service.RatingScaleService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bracits.epms.domain.RatingScale}.
 */
@Service
@Transactional
public class RatingScaleServiceImpl implements RatingScaleService {

    private static final Logger log = LoggerFactory.getLogger(RatingScaleServiceImpl.class);

    private final RatingScaleRepository ratingScaleRepository;

    public RatingScaleServiceImpl(RatingScaleRepository ratingScaleRepository) {
        this.ratingScaleRepository = ratingScaleRepository;
    }

    @Override
    public RatingScale save(RatingScale ratingScale) {
        log.debug("Request to save RatingScale : {}", ratingScale);
        return ratingScaleRepository.save(ratingScale);
    }

    @Override
    public RatingScale update(RatingScale ratingScale) {
        log.debug("Request to update RatingScale : {}", ratingScale);
        return ratingScaleRepository.save(ratingScale);
    }

    @Override
    public Optional<RatingScale> partialUpdate(RatingScale ratingScale) {
        log.debug("Request to partially update RatingScale : {}", ratingScale);

        return ratingScaleRepository
            .findById(ratingScale.getId())
            .map(existingRatingScale -> {
                if (ratingScale.getScaletype() != null) {
                    existingRatingScale.setScaletype(ratingScale.getScaletype());
                }
                if (ratingScale.getRatingscales() != null) {
                    existingRatingScale.setRatingscales(ratingScale.getRatingscales());
                }

                return existingRatingScale;
            })
            .map(ratingScaleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingScale> findAll() {
        log.debug("Request to get all RatingScales");
        return ratingScaleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RatingScale> findOne(Long id) {
        log.debug("Request to get RatingScale : {}", id);
        return ratingScaleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete RatingScale : {}", id);
        ratingScaleRepository.deleteById(id);
    }
}
