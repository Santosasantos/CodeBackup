package com.bracits.epms.web.rest;

import com.bracits.epms.domain.FeedbackSubType;
import com.bracits.epms.repository.FeedbackSubTypeRepository;
import com.bracits.epms.service.FeedbackSubTypeService;
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
 * REST controller for managing {@link com.bracits.epms.domain.FeedbackSubType}.
 */
@RestController
@RequestMapping("/api/feedback-sub-types")
public class FeedbackSubTypeResource {

    private static final Logger log = LoggerFactory.getLogger(FeedbackSubTypeResource.class);

    private static final String ENTITY_NAME = "feedbackSubType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackSubTypeService feedbackSubTypeService;

    private final FeedbackSubTypeRepository feedbackSubTypeRepository;

    public FeedbackSubTypeResource(FeedbackSubTypeService feedbackSubTypeService, FeedbackSubTypeRepository feedbackSubTypeRepository) {
        this.feedbackSubTypeService = feedbackSubTypeService;
        this.feedbackSubTypeRepository = feedbackSubTypeRepository;
    }

    /**
     * {@code POST  /feedback-sub-types} : Create a new feedbackSubType.
     *
     * @param feedbackSubType the feedbackSubType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedbackSubType, or with status {@code 400 (Bad Request)} if the feedbackSubType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeedbackSubType> createFeedbackSubType(@Valid @RequestBody FeedbackSubType feedbackSubType)
        throws URISyntaxException {
        log.debug("REST request to save FeedbackSubType : {}", feedbackSubType);
        if (feedbackSubType.getId() != null) {
            throw new BadRequestAlertException("A new feedbackSubType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feedbackSubType = feedbackSubTypeService.save(feedbackSubType);
        return ResponseEntity.created(new URI("/api/feedback-sub-types/" + feedbackSubType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feedbackSubType.getId().toString()))
            .body(feedbackSubType);
    }

    /**
     * {@code PUT  /feedback-sub-types/:id} : Updates an existing feedbackSubType.
     *
     * @param id the id of the feedbackSubType to save.
     * @param feedbackSubType the feedbackSubType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackSubType,
     * or with status {@code 400 (Bad Request)} if the feedbackSubType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedbackSubType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeedbackSubType> updateFeedbackSubType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeedbackSubType feedbackSubType
    ) throws URISyntaxException {
        log.debug("REST request to update FeedbackSubType : {}, {}", id, feedbackSubType);
        if (feedbackSubType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackSubType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackSubTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feedbackSubType = feedbackSubTypeService.update(feedbackSubType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackSubType.getId().toString()))
            .body(feedbackSubType);
    }

    /**
     * {@code PATCH  /feedback-sub-types/:id} : Partial updates given fields of an existing feedbackSubType, field will ignore if it is null
     *
     * @param id the id of the feedbackSubType to save.
     * @param feedbackSubType the feedbackSubType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedbackSubType,
     * or with status {@code 400 (Bad Request)} if the feedbackSubType is not valid,
     * or with status {@code 404 (Not Found)} if the feedbackSubType is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedbackSubType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeedbackSubType> partialUpdateFeedbackSubType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeedbackSubType feedbackSubType
    ) throws URISyntaxException {
        log.debug("REST request to partial update FeedbackSubType partially : {}, {}", id, feedbackSubType);
        if (feedbackSubType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedbackSubType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackSubTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeedbackSubType> result = feedbackSubTypeService.partialUpdate(feedbackSubType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedbackSubType.getId().toString())
        );
    }

    /**
     * {@code GET  /feedback-sub-types} : get all the feedbackSubTypes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbackSubTypes in body.
     */
    @GetMapping("")
    public List<FeedbackSubType> getAllFeedbackSubTypes(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all FeedbackSubTypes");
        return feedbackSubTypeService.findAll();
    }

    /**
     * {@code GET  /feedback-sub-types/:id} : get the "id" feedbackSubType.
     *
     * @param id the id of the feedbackSubType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackSubType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackSubType> getFeedbackSubType(@PathVariable("id") Long id) {
        log.debug("REST request to get FeedbackSubType : {}", id);
        Optional<FeedbackSubType> feedbackSubType = feedbackSubTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedbackSubType);
    }

    /**
     * {@code DELETE  /feedback-sub-types/:id} : delete the "id" feedbackSubType.
     *
     * @param id the id of the feedbackSubType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedbackSubType(@PathVariable("id") Long id) {
        log.debug("REST request to delete FeedbackSubType : {}", id);
        feedbackSubTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
