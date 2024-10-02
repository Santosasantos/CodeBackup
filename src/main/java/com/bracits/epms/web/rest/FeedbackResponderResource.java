package com.bracits.epms.web.rest;

import com.bracits.epms.domain.Feedback;
import com.bracits.epms.domain.FeedbackResponder;
import com.bracits.epms.domain.enumeration.FeedbackStatus;
import com.bracits.epms.repository.FeedbackResponderRepository;
import com.bracits.epms.service.FeedbackResponderService;
import com.bracits.epms.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.bracits.epms.domain.FeedbackResponder}.
 */
@RestController
@RequestMapping("/api/feedback-responders")
public class FeedbackResponderResource {

    private static final Logger log = LoggerFactory.getLogger(FeedbackResponderResource.class);

    private static final String ENTITY_NAME = "feedbackResponder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackResponderService feedbackResponderService;

    private final FeedbackResponderRepository feedbackResponderRepository;

    public FeedbackResponderResource(
        FeedbackResponderService feedbackResponderService,
        FeedbackResponderRepository feedbackResponderRepository
    ) {
        this.feedbackResponderService = feedbackResponderService;
        this.feedbackResponderRepository = feedbackResponderRepository;
    }

    /**
     * {@code POST  /feedback-responders} : Create a new feedbackResponder.
     *
     * @param feedbackResponder the feedbackResponder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackResponder, or with status {@code 400 (Bad Request)} if the feedbackResponder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeedbackResponder> createFeedbackResponder(@Valid @RequestBody FeedbackResponder feedbackResponder)
        throws URISyntaxException {
        log.debug("REST request to save FeedbackResponder : {}", feedbackResponder);
        if (feedbackResponder.getId() != null) {
            throw new BadRequestAlertException("A new feedbackResponder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feedbackResponder = feedbackResponderService.save(feedbackResponder);
        return ResponseEntity.created(new URI("/api/feedback-responders/" + feedbackResponder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feedbackResponder.getId().toString()))
            .body(feedbackResponder);
    }

    /**
     * {@code PUT  /feedback-responders/:id} : Updates an existing feedbackResponder.
     *
     * @param id the id of the feedbackResponder to save.
     * @param feedbackResponder the feedbackResponder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponder,
     * or with status {@code 400 (Bad Request)} if the feedbackResponder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackResponder> updateFeedbackResponder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeedbackResponder feedbackResponder
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackResponder : {}, {}", id, feedbackResponder);
        if (feedbackResponder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackResponderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feedbackResponder = feedbackResponderService.update(feedbackResponder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackResponder.getId().toString()))
            .body(feedbackResponder);
    }

    /**
     * {@code PATCH  /feedback-responders/:id} : Partial updates given fields of an existing feedbackResponder, field will ignore if it is null
     *
     * @param id the id of the feedbackResponder to save.
     * @param feedbackResponder the feedbackResponder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackResponder,
     * or with status {@code 400 (Bad Request)} if the feedbackResponder is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackResponder is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackResponder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackResponder> partialUpdateFeedbackResponder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeedbackResponder feedbackResponder
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackResponder partially : {}, {}", id, feedbackResponder);
        if (feedbackResponder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackResponder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackResponderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackResponder> result = feedbackResponderService.partialUpdate(feedbackResponder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackResponder.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-responders} : get all the feedbackResponders.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackResponders in body.
     */
    @GetMapping("")
    public List<FeedbackResponder> getAllFeedbackResponders(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all FeedbackResponders");
        return feedbackResponderService.findAll();
    }

    /**
     * {@code GET  /feedback-responders/:id} : get the "id" feedbackResponder.
     *
     * @param id the id of the feedbackResponder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackResponder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponder> getFeedbackResponder(@PathVariable("id") Long id) {
        log.debug("REST request to get FeedbackResponder : {}", id);
        Optional<FeedbackResponder> feedbackResponder = feedbackResponderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackResponder);
    }

    @GetMapping("/responderswithfeedback/{feedbackid}")
    public List<FeedbackResponder> getFeedbackRespondersWithFeedback(@PathVariable Long feedbackid) {
        log.debug("REST request to get FeedbackResponders with feedback : {}", feedbackid);
        return feedbackResponderService.getFeedbackRespondersWithFeedback(feedbackid);
    }

    @GetMapping("/requester")
    public List<FeedbackResponder> getAllRequester(@RequestParam String responderpin, @RequestParam Integer year) {
        log.debug("REST request to get Feedback Requester with feedback responder pin and year: {}", responderpin, year);
        return feedbackResponderService.findAllRequester(responderpin, year);
    }

    @PutMapping("/{id}/update-responder-status")
    public ResponseEntity<FeedbackResponder> updateResponderStatus(@PathVariable Long id, @RequestParam FeedbackStatus responderStatus) {
        log.debug("REST request to update FeedbackResponder : {}", id);
        FeedbackResponder feedbackResponder = feedbackResponderService.updateResponderStatus(id, responderStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackResponder.getId().toString()))
            .body(feedbackResponder);
    }

    /**
     * {@code DELETE  /feedback-responders/:id} : delete the "id" feedbackResponder.
     *
     * @param id the id of the feedbackResponder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedbackResponder(@PathVariable("id") Long id) {
        log.debug("REST request to delete FeedbackResponder : {}", id);
        feedbackResponderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
