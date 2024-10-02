package com.bracits.epms.web.rest;

import com.bracits.epms.domain.FeedbackType;
import com.bracits.epms.repository.FeedbackTypeRepository;
import com.bracits.epms.service.FeedbackTypeService;
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
 * REST controller for managing {@link com.bracits.epms.domain.FeedbackType}.
 */
@RestController
@RequestMapping("/api/feedback-types")
public class FeedbackTypeResource {

    private static final Logger log = LoggerFactory.getLogger(FeedbackTypeResource.class);

    private static final String ENTITY_NAME = "feedbackType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackTypeService feedbackTypeService;

    private final FeedbackTypeRepository feedbackTypeRepository;

    public FeedbackTypeResource(FeedbackTypeService feedbackTypeService, FeedbackTypeRepository feedbackTypeRepository) {
        this.feedbackTypeService = feedbackTypeService;
        this.feedbackTypeRepository = feedbackTypeRepository;
    }

    /**
     * {@code POST  /feedback-types} : Create a new feedbackType.
     *
     * @param feedbackType the feedbackType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackType, or with status {@code 400 (Bad Request)} if the feedbackType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeedbackType> createFeedbackType(@Valid @RequestBody FeedbackType feedbackType) throws URISyntaxException {
        log.debug("REST request to save FeedbackType : {}", feedbackType);
        if (feedbackType.getId() != null) {
            throw new BadRequestAlertException("A new feedbackType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feedbackType = feedbackTypeService.save(feedbackType);
        return ResponseEntity.created(new URI("/api/feedback-types/" + feedbackType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feedbackType.getId().toString()))
            .body(feedbackType);
    }

    /**
     * {@code PUT  /feedback-types/:id} : Updates an existing feedbackType.
     *
     * @param id the id of the feedbackType to save.
     * @param feedbackType the feedbackType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackType,
     * or with status {@code 400 (Bad Request)} if the feedbackType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackType> updateFeedbackType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeedbackType feedbackType
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackType : {}, {}", id, feedbackType);
        if (feedbackType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feedbackType = feedbackTypeService.update(feedbackType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackType.getId().toString()))
            .body(feedbackType);
    }

    /**
     * {@code PATCH  /feedback-types/:id} : Partial updates given fields of an existing feedbackType, field will ignore if it is null
     *
     * @param id the id of the feedbackType to save.
     * @param feedbackType the feedbackType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackType,
     * or with status {@code 400 (Bad Request)} if the feedbackType is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackType is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackType> partialUpdateFeedbackType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeedbackType feedbackType
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackType partially : {}, {}", id, feedbackType);
        if (feedbackType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackType> result = feedbackTypeService.partialUpdate(feedbackType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackType.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-types} : get all the feedbackTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackTypes in body.
     */
    @GetMapping("")
    public List<FeedbackType> getAllFeedbackTypes() {
        log.debug("REST request to get all FeedbackTypes");
        return feedbackTypeService.findAll();
    }

    /**
     * {@code GET  /feedback-types/:id} : get the "id" feedbackType.
     *
     * @param id the id of the feedbackType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackType> getFeedbackType(@PathVariable("id") Long id) {
        log.debug("REST request to get FeedbackType : {}", id);
        Optional<FeedbackType> feedbackType = feedbackTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackType);
    }

    /**
     * {@code DELETE  /feedback-types/:id} : delete the "id" feedbackType.
     *
     * @param id the id of the feedbackType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedbackType(@PathVariable("id") Long id) {
        log.debug("REST request to delete FeedbackType : {}", id);
        feedbackTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
