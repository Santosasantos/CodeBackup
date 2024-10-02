package com.bracits.epms.web.rest;

import com.bracits.epms.domain.SkillDevelopmentDetails;
import com.bracits.epms.repository.SkillDevelopmentDetailsRepository;
import com.bracits.epms.service.SkillDevelopmentDetailsService;
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
 * REST controller for managing {@link com.bracits.epms.domain.SkillDevelopmentDetails}.
 */
@RestController
@RequestMapping("/api/skill-development-details")
public class SkillDevelopmentDetailsResource {

    private static final Logger log = LoggerFactory.getLogger(SkillDevelopmentDetailsResource.class);

    private static final String ENTITY_NAME = "skillDevelopmentDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkillDevelopmentDetailsService skillDevelopmentDetailsService;

    private final SkillDevelopmentDetailsRepository skillDevelopmentDetailsRepository;

    public SkillDevelopmentDetailsResource(
        SkillDevelopmentDetailsService skillDevelopmentDetailsService,
        SkillDevelopmentDetailsRepository skillDevelopmentDetailsRepository
    ) {
        this.skillDevelopmentDetailsService = skillDevelopmentDetailsService;
        this.skillDevelopmentDetailsRepository = skillDevelopmentDetailsRepository;
    }

    /**
     * {@code POST  /skill-development-details} : Create a new skillDevelopmentDetails.
     *
     * @param skillDevelopmentDetails the skillDevelopmentDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillDevelopmentDetails, or with status {@code 400 (Bad Request)} if the skillDevelopmentDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SkillDevelopmentDetails> createSkillDevelopmentDetails(
        @RequestBody SkillDevelopmentDetails skillDevelopmentDetails
    ) throws URISyntaxException {
        log.debug("REST request to save SkillDevelopmentDetails : {}", skillDevelopmentDetails);
        if (skillDevelopmentDetails.getId() != null) {
            throw new BadRequestAlertException("A new skillDevelopmentDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        skillDevelopmentDetails = skillDevelopmentDetailsService.save(skillDevelopmentDetails);
        return ResponseEntity.created(new URI("/api/skill-development-details/" + skillDevelopmentDetails.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, skillDevelopmentDetails.getId().toString()))
            .body(skillDevelopmentDetails);
    }

    /**
     * {@code PUT  /skill-development-details/:id} : Updates an existing skillDevelopmentDetails.
     *
     * @param id the id of the skillDevelopmentDetails to save.
     * @param skillDevelopmentDetails the skillDevelopmentDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillDevelopmentDetails,
     * or with status {@code 400 (Bad Request)} if the skillDevelopmentDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillDevelopmentDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SkillDevelopmentDetails> updateSkillDevelopmentDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SkillDevelopmentDetails skillDevelopmentDetails
    ) throws URISyntaxException {
        log.debug("REST request to update SkillDevelopmentDetails : {}, {}", id, skillDevelopmentDetails);
        if (skillDevelopmentDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillDevelopmentDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillDevelopmentDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        skillDevelopmentDetails = skillDevelopmentDetailsService.update(skillDevelopmentDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillDevelopmentDetails.getId().toString()))
            .body(skillDevelopmentDetails);
    }

    /**
     * {@code PATCH  /skill-development-details/:id} : Partial updates given fields of an existing skillDevelopmentDetails, field will ignore if it is null
     *
     * @param id the id of the skillDevelopmentDetails to save.
     * @param skillDevelopmentDetails the skillDevelopmentDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillDevelopmentDetails,
     * or with status {@code 400 (Bad Request)} if the skillDevelopmentDetails is not valid,
     * or with status {@code 404 (Not Found)} if the skillDevelopmentDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the skillDevelopmentDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SkillDevelopmentDetails> partialUpdateSkillDevelopmentDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SkillDevelopmentDetails skillDevelopmentDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update SkillDevelopmentDetails partially : {}, {}", id, skillDevelopmentDetails);
        if (skillDevelopmentDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillDevelopmentDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillDevelopmentDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SkillDevelopmentDetails> result = skillDevelopmentDetailsService.partialUpdate(skillDevelopmentDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillDevelopmentDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /skill-development-details} : get all the skillDevelopmentDetails.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skillDevelopmentDetails in body.
     */
    @GetMapping("")
    public List<SkillDevelopmentDetails> getAllSkillDevelopmentDetails(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all SkillDevelopmentDetails");
        return skillDevelopmentDetailsService.findAll();
    }

    /**
     * {@code GET  /skill-development-details/:id} : get the "id" skillDevelopmentDetails.
     *
     * @param id the id of the skillDevelopmentDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillDevelopmentDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillDevelopmentDetails> getSkillDevelopmentDetails(@PathVariable("id") Long id) {
        log.debug("REST request to get SkillDevelopmentDetails : {}", id);
        Optional<SkillDevelopmentDetails> skillDevelopmentDetails = skillDevelopmentDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillDevelopmentDetails);
    }

    /**
     * {@code DELETE  /skill-development-details/:id} : delete the "id" skillDevelopmentDetails.
     *
     * @param id the id of the skillDevelopmentDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillDevelopmentDetails(@PathVariable("id") Long id) {
        log.debug("REST request to delete SkillDevelopmentDetails : {}", id);
        skillDevelopmentDetailsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
