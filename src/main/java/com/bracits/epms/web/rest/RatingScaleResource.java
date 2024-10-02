package com.bracits.epms.web.rest;

import com.bracits.epms.domain.RatingScale;
import com.bracits.epms.repository.RatingScaleRepository;
import com.bracits.epms.service.RatingScaleService;
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
 * REST controller for managing {@link com.bracits.epms.domain.RatingScale}.
 */
@RestController
@RequestMapping("/api/rating-scales")
public class RatingScaleResource {

    private static final Logger log = LoggerFactory.getLogger(RatingScaleResource.class);

    private static final String ENTITY_NAME = "ratingScale";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RatingScaleService ratingScaleService;

    private final RatingScaleRepository ratingScaleRepository;

    public RatingScaleResource(RatingScaleService ratingScaleService, RatingScaleRepository ratingScaleRepository) {
        this.ratingScaleService = ratingScaleService;
        this.ratingScaleRepository = ratingScaleRepository;
    }

    /**
     * {@code POST  /rating-scales} : Create a new ratingScale.
     *
     * @param ratingScale the ratingScale to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ratingScale, or with status {@code 400 (Bad Request)} if the ratingScale has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RatingScale> createRatingScale(@Valid @RequestBody RatingScale ratingScale) throws URISyntaxException {
        log.debug("REST request to save RatingScale : {}", ratingScale);
        if (ratingScale.getId() != null) {
            throw new BadRequestAlertException("A new ratingScale cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ratingScale = ratingScaleService.save(ratingScale);
        return ResponseEntity.created(new URI("/api/rating-scales/" + ratingScale.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ratingScale.getId().toString()))
            .body(ratingScale);
    }

    /**
     * {@code PUT  /rating-scales/:id} : Updates an existing ratingScale.
     *
     * @param id the id of the ratingScale to save.
     * @param ratingScale the ratingScale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ratingScale,
     * or with status {@code 400 (Bad Request)} if the ratingScale is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ratingScale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RatingScale> updateRatingScale(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RatingScale ratingScale
    ) throws URISyntaxException {
        log.debug("REST request to update RatingScale : {}, {}", id, ratingScale);
        if (ratingScale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ratingScale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingScaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ratingScale = ratingScaleService.update(ratingScale);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ratingScale.getId().toString()))
            .body(ratingScale);
    }

    /**
     * {@code PATCH  /rating-scales/:id} : Partial updates given fields of an existing ratingScale, field will ignore if it is null
     *
     * @param id the id of the ratingScale to save.
     * @param ratingScale the ratingScale to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ratingScale,
     * or with status {@code 400 (Bad Request)} if the ratingScale is not valid,
     * or with status {@code 404 (Not Found)} if the ratingScale is not found,
     * or with status {@code 500 (Internal Server Error)} if the ratingScale couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RatingScale> partialUpdateRatingScale(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RatingScale ratingScale
    ) throws URISyntaxException {
        log.debug("REST request to partial update RatingScale partially : {}, {}", id, ratingScale);
        if (ratingScale.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ratingScale.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ratingScaleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RatingScale> result = ratingScaleService.partialUpdate(ratingScale);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ratingScale.getId().toString())
        );
    }

    /**
     * {@code GET  /rating-scales} : get all the ratingScales.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ratingScales in body.
     */
    @GetMapping("")
    public List<RatingScale> getAllRatingScales() {
        log.debug("REST request to get all RatingScales");
        return ratingScaleService.findAll();
    }

    /**
     * {@code GET  /rating-scales/:id} : get the "id" ratingScale.
     *
     * @param id the id of the ratingScale to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ratingScale, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RatingScale> getRatingScale(@PathVariable("id") Long id) {
        log.debug("REST request to get RatingScale : {}", id);
        Optional<RatingScale> ratingScale = ratingScaleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ratingScale);
    }

    /**
     * {@code DELETE  /rating-scales/:id} : delete the "id" ratingScale.
     *
     * @param id the id of the ratingScale to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRatingScale(@PathVariable("id") Long id) {
        log.debug("REST request to delete RatingScale : {}", id);
        ratingScaleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
