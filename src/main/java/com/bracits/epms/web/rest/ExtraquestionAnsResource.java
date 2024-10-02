package com.bracits.epms.web.rest;

import com.bracits.epms.domain.ExtraquestionAns;
import com.bracits.epms.repository.ExtraquestionAnsRepository;
import com.bracits.epms.service.ExtraquestionAnsService;
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
 * REST controller for managing {@link com.bracits.epms.domain.ExtraquestionAns}.
 */
@RestController
@RequestMapping("/api/extraquestion-ans")
public class ExtraquestionAnsResource {

    private static final Logger log = LoggerFactory.getLogger(ExtraquestionAnsResource.class);

    private static final String ENTITY_NAME = "extraquestionAns";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtraquestionAnsService extraquestionAnsService;

    private final ExtraquestionAnsRepository extraquestionAnsRepository;

    public ExtraquestionAnsResource(
        ExtraquestionAnsService extraquestionAnsService,
        ExtraquestionAnsRepository extraquestionAnsRepository
    ) {
        this.extraquestionAnsService = extraquestionAnsService;
        this.extraquestionAnsRepository = extraquestionAnsRepository;
    }

    /**
     * {@code POST  /extraquestion-ans} : Create a new extraquestionAns.
     *
     * @param extraquestionAns the extraquestionAns to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extraquestionAns, or with status {@code 400 (Bad Request)} if the extraquestionAns has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExtraquestionAns> createExtraquestionAns(@Valid @RequestBody ExtraquestionAns extraquestionAns)
        throws URISyntaxException {
        log.debug("REST request to save ExtraquestionAns : {}", extraquestionAns);
        if (extraquestionAns.getId() != null) {
            throw new BadRequestAlertException("A new extraquestionAns cannot already have an ID", ENTITY_NAME, "idexists");
        }
        extraquestionAns = extraquestionAnsService.save(extraquestionAns);
        return ResponseEntity.created(new URI("/api/extraquestion-ans/" + extraquestionAns.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, extraquestionAns.getId().toString()))
            .body(extraquestionAns);
    }

    /**
     * {@code PUT  /extraquestion-ans/:id} : Updates an existing extraquestionAns.
     *
     * @param id the id of the extraquestionAns to save.
     * @param extraquestionAns the extraquestionAns to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraquestionAns,
     * or with status {@code 400 (Bad Request)} if the extraquestionAns is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extraquestionAns couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExtraquestionAns> updateExtraquestionAns(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExtraquestionAns extraquestionAns
    ) throws URISyntaxException {
        log.debug("REST request to update ExtraquestionAns : {}, {}", id, extraquestionAns);
        if (extraquestionAns.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraquestionAns.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraquestionAnsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        extraquestionAns = extraquestionAnsService.update(extraquestionAns);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extraquestionAns.getId().toString()))
            .body(extraquestionAns);
    }

    /**
     * {@code PATCH  /extraquestion-ans/:id} : Partial updates given fields of an existing extraquestionAns, field will ignore if it is null
     *
     * @param id the id of the extraquestionAns to save.
     * @param extraquestionAns the extraquestionAns to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraquestionAns,
     * or with status {@code 400 (Bad Request)} if the extraquestionAns is not valid,
     * or with status {@code 404 (Not Found)} if the extraquestionAns is not found,
     * or with status {@code 500 (Internal Server Error)} if the extraquestionAns couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExtraquestionAns> partialUpdateExtraquestionAns(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExtraquestionAns extraquestionAns
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExtraquestionAns partially : {}, {}", id, extraquestionAns);
        if (extraquestionAns.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraquestionAns.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraquestionAnsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExtraquestionAns> result = extraquestionAnsService.partialUpdate(extraquestionAns);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extraquestionAns.getId().toString())
        );
    }

    /**
     * {@code GET  /extraquestion-ans} : get all the extraquestionAns.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extraquestionAns in body.
     */
    @GetMapping("")
    public List<ExtraquestionAns> getAllExtraquestionAns() {
        log.debug("REST request to get all ExtraquestionAns");
        return extraquestionAnsService.findAll();
    }

    /**
     * {@code GET  /extraquestion-ans/:id} : get the "id" extraquestionAns.
     *
     * @param id the id of the extraquestionAns to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extraquestionAns, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtraquestionAns> getExtraquestionAns(@PathVariable("id") Long id) {
        log.debug("REST request to get ExtraquestionAns : {}", id);
        Optional<ExtraquestionAns> extraquestionAns = extraquestionAnsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extraquestionAns);
    }

    /**
     * {@code DELETE  /extraquestion-ans/:id} : delete the "id" extraquestionAns.
     *
     * @param id the id of the extraquestionAns to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtraquestionAns(@PathVariable("id") Long id) {
        log.debug("REST request to delete ExtraquestionAns : {}", id);
        extraquestionAnsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
