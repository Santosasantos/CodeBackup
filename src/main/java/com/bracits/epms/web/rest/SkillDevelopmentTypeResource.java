package com.bracits.epms.web.rest;

import com.bracits.epms.domain.SkillDevelopmentType;
import com.bracits.epms.repository.SkillDevelopmentTypeRepository;
import com.bracits.epms.service.SkillDevelopmentTypeService;
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
 * REST controller for managing {@link com.bracits.epms.domain.SkillDevelopmentType}.
 */
@RestController
@RequestMapping("/api/skill-development-types")
public class SkillDevelopmentTypeResource {

    private static final Logger log = LoggerFactory.getLogger(SkillDevelopmentTypeResource.class);

    private static final String ENTITY_NAME = "skillDevelopmentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkillDevelopmentTypeService skillDevelopmentTypeService;

    private final SkillDevelopmentTypeRepository skillDevelopmentTypeRepository;

    public SkillDevelopmentTypeResource(
        SkillDevelopmentTypeService skillDevelopmentTypeService,
        SkillDevelopmentTypeRepository skillDevelopmentTypeRepository
    ) {
        this.skillDevelopmentTypeService = skillDevelopmentTypeService;
        this.skillDevelopmentTypeRepository = skillDevelopmentTypeRepository;
    }

    /**
     * {@code POST  /skill-development-types} : Create a new skillDevelopmentType.
     *
     * @param skillDevelopmentType the skillDevelopmentType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillDevelopmentType, or with status {@code 400 (Bad Request)} if the skillDevelopmentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SkillDevelopmentType> createSkillDevelopmentType(@Valid @RequestBody SkillDevelopmentType skillDevelopmentType)
        throws URISyntaxException {
        log.debug("REST request to save SkillDevelopmentType : {}", skillDevelopmentType);
        if (skillDevelopmentType.getId() != null) {
            throw new BadRequestAlertException("A new skillDevelopmentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        skillDevelopmentType = skillDevelopmentTypeService.save(skillDevelopmentType);
        return ResponseEntity.created(new URI("/api/skill-development-types/" + skillDevelopmentType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, skillDevelopmentType.getId().toString()))
            .body(skillDevelopmentType);
    }

    /**
     * {@code PUT  /skill-development-types/:id} : Updates an existing skillDevelopmentType.
     *
     * @param id the id of the skillDevelopmentType to save.
     * @param skillDevelopmentType the skillDevelopmentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillDevelopmentType,
     * or with status {@code 400 (Bad Request)} if the skillDevelopmentType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillDevelopmentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SkillDevelopmentType> updateSkillDevelopmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SkillDevelopmentType skillDevelopmentType
    ) throws URISyntaxException {
        log.debug("REST request to update SkillDevelopmentType : {}, {}", id, skillDevelopmentType);
        if (skillDevelopmentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillDevelopmentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillDevelopmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        skillDevelopmentType = skillDevelopmentTypeService.update(skillDevelopmentType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillDevelopmentType.getId().toString()))
            .body(skillDevelopmentType);
    }

    /**
     * {@code PATCH  /skill-development-types/:id} : Partial updates given fields of an existing skillDevelopmentType, field will ignore if it is null
     *
     * @param id the id of the skillDevelopmentType to save.
     * @param skillDevelopmentType the skillDevelopmentType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillDevelopmentType,
     * or with status {@code 400 (Bad Request)} if the skillDevelopmentType is not valid,
     * or with status {@code 404 (Not Found)} if the skillDevelopmentType is not found,
     * or with status {@code 500 (Internal Server Error)} if the skillDevelopmentType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SkillDevelopmentType> partialUpdateSkillDevelopmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SkillDevelopmentType skillDevelopmentType
    ) throws URISyntaxException {
        log.debug("REST request to partial update SkillDevelopmentType partially : {}, {}", id, skillDevelopmentType);
        if (skillDevelopmentType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, skillDevelopmentType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!skillDevelopmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SkillDevelopmentType> result = skillDevelopmentTypeService.partialUpdate(skillDevelopmentType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillDevelopmentType.getId().toString())
        );
    }

    /**
     * {@code GET  /skill-development-types} : get all the skillDevelopmentTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skillDevelopmentTypes in body.
     */
    @GetMapping("")
    public List<SkillDevelopmentType> getAllSkillDevelopmentTypes() {
        log.debug("REST request to get all SkillDevelopmentTypes");
        return skillDevelopmentTypeService.findAll();
    }

    /**
     * {@code GET  /skill-development-types/:id} : get the "id" skillDevelopmentType.
     *
     * @param id the id of the skillDevelopmentType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillDevelopmentType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SkillDevelopmentType> getSkillDevelopmentType(@PathVariable("id") Long id) {
        log.debug("REST request to get SkillDevelopmentType : {}", id);
        Optional<SkillDevelopmentType> skillDevelopmentType = skillDevelopmentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillDevelopmentType);
    }

    /**
     * {@code DELETE  /skill-development-types/:id} : delete the "id" skillDevelopmentType.
     *
     * @param id the id of the skillDevelopmentType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillDevelopmentType(@PathVariable("id") Long id) {
        log.debug("REST request to delete SkillDevelopmentType : {}", id);
        skillDevelopmentTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
