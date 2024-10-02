package com.bracits.epms.web.rest;

import com.bracits.epms.domain.FeedbackDetails;
import com.bracits.epms.repository.FeedbackDetailsRepository;
import com.bracits.epms.service.FeedbackDetailsService;
import com.bracits.epms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bracits.epms.domain.FeedbackDetails}.
 */
@RestController
@RequestMapping("/api/feedback-details")
public class FeedbackDetailsResource {

    private static final Logger log = LoggerFactory.getLogger(FeedbackDetailsResource.class);

    private static final String ENTITY_NAME = "feedbackDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackDetailsService feedbackDetailsService;

    private final FeedbackDetailsRepository feedbackDetailsRepository;

    public FeedbackDetailsResource(FeedbackDetailsService feedbackDetailsService, FeedbackDetailsRepository feedbackDetailsRepository) {
        this.feedbackDetailsService = feedbackDetailsService;
        this.feedbackDetailsRepository = feedbackDetailsRepository;
    }

    /**
     * {@code POST  /feedback-details} : Create a new feedbackDetails.
     *
     * @param feedbackDetails the feedbackDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackDetails, or with status {@code 400 (Bad Request)} if the feedbackDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeedbackDetails> createFeedbackDetails(@RequestBody FeedbackDetails feedbackDetails) throws URISyntaxException {
        log.debug("REST request to save FeedbackDetails : {}", feedbackDetails);
        if (feedbackDetails.getId() != null) {
            throw new BadRequestAlertException("A new feedbackDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feedbackDetails = feedbackDetailsService.save(feedbackDetails);
        return ResponseEntity.created(new URI("/api/feedback-details/" + feedbackDetails.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feedbackDetails.getId().toString()))
            .body(feedbackDetails);
    }

    /**
     * {@code PUT  /feedback-details/:id} : Updates an existing feedbackDetails.
     *
     * @param id the id of the feedbackDetails to save.
     * @param feedbackDetails the feedbackDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackDetails,
     * or with status {@code 400 (Bad Request)} if the feedbackDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDetails> updateFeedbackDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackDetails feedbackDetails
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackDetails : {}, {}", id, feedbackDetails);
        if (feedbackDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feedbackDetails = feedbackDetailsService.update(feedbackDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackDetails.getId().toString()))
            .body(feedbackDetails);
    }

    /**
     * {@code PATCH  /feedback-details/:id} : Partial updates given fields of an existing feedbackDetails, field will ignore if it is null
     *
     * @param id the id of the feedbackDetails to save.
     * @param feedbackDetails the feedbackDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackDetails,
     * or with status {@code 400 (Bad Request)} if the feedbackDetails is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackDetails> partialUpdateFeedbackDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FeedbackDetails feedbackDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackDetails partially : {}, {}", id, feedbackDetails);
        if (feedbackDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackDetails> result = feedbackDetailsService.partialUpdate(feedbackDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-details} : get all the feedbackDetails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackDetails in body.
     */
    @GetMapping("")
    public List<FeedbackDetails> getAllFeedbackDetails(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all FeedbackDetails");
        return feedbackDetailsService.findAll();
    }

    /**
     * {@code GET  /feedback-details/:id} : get the "id" feedbackDetails.
     *
     * @param id the id of the feedbackDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDetails> getFeedbackDetails(@PathVariable("id") Long id) {
        log.debug("REST request to get FeedbackDetails : {}", id);
        Optional<FeedbackDetails> feedbackDetails = feedbackDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackDetails);
    }

    @GetMapping("/fetchratingfromothers")
    public ResponseEntity<Double> fetchRating(@RequestParam String pin, @RequestParam String feedbacksubname, @RequestParam Integer year) {
        log.debug("REST request to get average rating for pin : {} and feedbacksubname : {}", pin, feedbacksubname, year);
        Optional<Double> rating = feedbackDetailsService.findAverageRatingfromOther(pin, feedbacksubname, year);
        return ResponseUtil.wrapOrNotFound(rating);
    }

    @GetMapping("/fetchratingforself")
    public ResponseEntity<Integer> fetchSelfRating(
        @RequestParam String pin,
        @RequestParam String feedbacksubname,
        @RequestParam Integer year
    ) {
        log.debug("REST request to get rating for pin : {} and feedbacksubname : {}", pin, feedbacksubname, year);
        Optional<Integer> rating = feedbackDetailsService.findSelfRating(pin, feedbacksubname, year);
        return ResponseUtil.wrapOrNotFound(rating);
    }

    /**
     * {@code DELETE  /feedback-details/:id} : delete the "id" feedbackDetails.
     *
     * @param id the id of the feedbackDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedbackDetails(@PathVariable("id") Long id) {
        log.debug("REST request to delete FeedbackDetails : {}", id);
        feedbackDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
