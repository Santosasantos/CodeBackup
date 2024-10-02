package com.bracits.epms.web.rest;

import com.bracits.epms.domain.Extraquestion;
import com.bracits.epms.repository.ExtraquestionRepository;
import com.bracits.epms.service.ExtraquestionService;
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
 * REST controller for managing {@link com.bracits.epms.domain.Extraquestion}.
 */
@RestController
@RequestMapping("/api/extraquestions")
public class ExtraquestionResource {

    private static final Logger log = LoggerFactory.getLogger(ExtraquestionResource.class);

    private static final String ENTITY_NAME = "extraquestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtraquestionService extraquestionService;

    private final ExtraquestionRepository extraquestionRepository;

    public ExtraquestionResource(ExtraquestionService extraquestionService, ExtraquestionRepository extraquestionRepository) {
        this.extraquestionService = extraquestionService;
        this.extraquestionRepository = extraquestionRepository;
    }

    /**
     * {@code POST  /extraquestions} : Create a new extraquestion.
     *
     * @param extraquestion the extraquestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extraquestion, or with status {@code 400 (Bad Request)} if the extraquestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Extraquestion> createExtraquestion(@Valid @RequestBody Extraquestion extraquestion) throws URISyntaxException {
        log.debug("REST request to save Extraquestion : {}", extraquestion);
        if (extraquestion.getId() != null) {
            throw new BadRequestAlertException("A new extraquestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        extraquestion = extraquestionService.save(extraquestion);
        return ResponseEntity.created(new URI("/api/extraquestions/" + extraquestion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, extraquestion.getId().toString()))
            .body(extraquestion);
    }

    /**
     * {@code PUT  /extraquestions/:id} : Updates an existing extraquestion.
     *
     * @param id the id of the extraquestion to save.
     * @param extraquestion the extraquestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraquestion,
     * or with status {@code 400 (Bad Request)} if the extraquestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extraquestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Extraquestion> updateExtraquestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Extraquestion extraquestion
    ) throws URISyntaxException {
        log.debug("REST request to update Extraquestion : {}, {}", id, extraquestion);
        if (extraquestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraquestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraquestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        extraquestion = extraquestionService.update(extraquestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extraquestion.getId().toString()))
            .body(extraquestion);
    }

    /**
     * {@code PATCH  /extraquestions/:id} : Partial updates given fields of an existing extraquestion, field will ignore if it is null
     *
     * @param id the id of the extraquestion to save.
     * @param extraquestion the extraquestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extraquestion,
     * or with status {@code 400 (Bad Request)} if the extraquestion is not valid,
     * or with status {@code 404 (Not Found)} if the extraquestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the extraquestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Extraquestion> partialUpdateExtraquestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Extraquestion extraquestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Extraquestion partially : {}, {}", id, extraquestion);
        if (extraquestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extraquestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extraquestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Extraquestion> result = extraquestionService.partialUpdate(extraquestion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extraquestion.getId().toString())
        );
    }

    /**
     * {@code GET  /extraquestions} : get all the extraquestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extraquestions in body.
     */
    @GetMapping("")
    public List<Extraquestion> getAllExtraquestions() {
        log.debug("REST request to get all Extraquestions");
        return extraquestionService.findAll();
    }

    /**
     * {@code GET  /extraquestions/:id} : get the "id" extraquestion.
     *
     * @param id the id of the extraquestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extraquestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Extraquestion> getExtraquestion(@PathVariable("id") Long id) {
        log.debug("REST request to get Extraquestion : {}", id);
        Optional<Extraquestion> extraquestion = extraquestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extraquestion);
    }

    @GetMapping("/feedback")
    public List<Extraquestion> getExtraquestionByFeedbackId(@RequestParam Long feedbackId) {
        log.debug("REST request to get Extraquestion by feedbackId : {}", feedbackId);
        return extraquestionService.findByFeedbackId(feedbackId);
    }

    /**
     * {@code DELETE  /extraquestions/:id} : delete the "id" extraquestion.
     *
     * @param id the id of the extraquestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtraquestion(@PathVariable("id") Long id) {
        log.debug("REST request to delete Extraquestion : {}", id);
        extraquestionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
