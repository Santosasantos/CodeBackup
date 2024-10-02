package com.bracits.epms.web.rest;

import com.bracits.epms.domain.TeachOther;
import com.bracits.epms.repository.TeachOtherRepository;
import com.bracits.epms.service.TeachOtherService;
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
 * REST controller for managing {@link com.bracits.epms.domain.TeachOther}.
 */
@RestController
@RequestMapping("/api/teach-others")
public class TeachOtherResource {

    private static final Logger log = LoggerFactory.getLogger(TeachOtherResource.class);

    private static final String ENTITY_NAME = "teachOther";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TeachOtherService teachOtherService;

    private final TeachOtherRepository teachOtherRepository;

    public TeachOtherResource(TeachOtherService teachOtherService, TeachOtherRepository teachOtherRepository) {
        this.teachOtherService = teachOtherService;
        this.teachOtherRepository = teachOtherRepository;
    }

    /**
     * {@code POST  /teach-others} : Create a new teachOther.
     *
     * @param teachOther the teachOther to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new teachOther, or with status {@code 400 (Bad Request)} if the teachOther has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TeachOther> createTeachOther(@Valid @RequestBody TeachOther teachOther) throws URISyntaxException {
        log.debug("REST request to save TeachOther : {}", teachOther);
        if (teachOther.getId() != null) {
            throw new BadRequestAlertException("A new teachOther cannot already have an ID", ENTITY_NAME, "idexists");
        }
        teachOther = teachOtherService.save(teachOther);
        return ResponseEntity.created(new URI("/api/teach-others/" + teachOther.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teachOther.getId().toString()))
            .body(teachOther);
    }

    /**
     * {@code PUT  /teach-others/:id} : Updates an existing teachOther.
     *
     * @param id the id of the teachOther to save.
     * @param teachOther the teachOther to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachOther,
     * or with status {@code 400 (Bad Request)} if the teachOther is not valid,
     * or with status {@code 500 (Internal Server Error)} if the teachOther couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeachOther> updateTeachOther(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TeachOther teachOther
    ) throws URISyntaxException {
        log.debug("REST request to update TeachOther : {}, {}", id, teachOther);
        if (teachOther.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teachOther.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teachOtherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        teachOther = teachOtherService.update(teachOther);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachOther.getId().toString()))
            .body(teachOther);
    }

    /**
     * {@code PATCH  /teach-others/:id} : Partial updates given fields of an existing teachOther, field will ignore if it is null
     *
     * @param id the id of the teachOther to save.
     * @param teachOther the teachOther to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated teachOther,
     * or with status {@code 400 (Bad Request)} if the teachOther is not valid,
     * or with status {@code 404 (Not Found)} if the teachOther is not found,
     * or with status {@code 500 (Internal Server Error)} if the teachOther couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TeachOther> partialUpdateTeachOther(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TeachOther teachOther
    ) throws URISyntaxException {
        log.debug("REST request to partial update TeachOther partially : {}, {}", id, teachOther);
        if (teachOther.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, teachOther.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!teachOtherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TeachOther> result = teachOtherService.partialUpdate(teachOther);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teachOther.getId().toString())
        );
    }

    /**
     * {@code GET  /teach-others} : get all the teachOthers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of teachOthers in body.
     */
    @GetMapping("")
    public List<TeachOther> getAllTeachOthers() {
        log.debug("REST request to get all TeachOthers");
        return teachOtherService.findAll();
    }

    /**
     * {@code GET  /teach-others/:id} : get the "id" teachOther.
     *
     * @param id the id of the teachOther to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the teachOther, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeachOther> getTeachOther(@PathVariable("id") Long id) {
        log.debug("REST request to get TeachOther : {}", id);
        Optional<TeachOther> teachOther = teachOtherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(teachOther);
    }

    /**
     * {@code DELETE  /teach-others/:id} : delete the "id" teachOther.
     *
     * @param id the id of the teachOther to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeachOther(@PathVariable("id") Long id) {
        log.debug("REST request to delete TeachOther : {}", id);
        teachOtherService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
