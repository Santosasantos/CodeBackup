package com.bracits.epms.web.rest;

import com.bracits.epms.domain.Employee;
import com.bracits.epms.domain.Feedback;
import com.bracits.epms.domain.FeedbackResponder;
import com.bracits.epms.domain.RatingScale;
import com.bracits.epms.domain.enumeration.FeedbackStatus;
import com.bracits.epms.domain.enumeration.ResponderCategory;
import com.bracits.epms.repository.FeedbackRepository;
import com.bracits.epms.service.EmployeeService;
import com.bracits.epms.service.FeedbackService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bracits.epms.domain.Feedback}.
 */
@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackResource {

    private static final Logger log = LoggerFactory.getLogger(FeedbackResource.class);

    private static final String ENTITY_NAME = "feedback";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeedbackService feedbackService;

    private final FeedbackRepository feedbackRepository;

    private final EmployeeService employeeService;

    public FeedbackResource(FeedbackService feedbackService, FeedbackRepository feedbackRepository, EmployeeService employeeService) {
        this.feedbackService = feedbackService;
        this.feedbackRepository = feedbackRepository;
        this.employeeService = employeeService;
    }

    /**
     * {@code POST  /feedbacks} : Create a new feedback.
     *
     * @param feedback the feedback to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feedback, or with status {@code 400 (Bad Request)} if the feedback has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Feedback> createFeedbackRequest(@Valid @RequestBody Feedback feedback) throws URISyntaxException {
        log.debug("REST request to create Feedback : {}", feedback);
        if (feedback.getId() != null) {
            throw new BadRequestAlertException("A new feedback cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Feedback result = feedbackService.createFeedbackRequest(feedback);
        return ResponseEntity.created(new URI("/api/feedbacks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<Feedback> updateStatus(@PathVariable Long id, @RequestParam FeedbackStatus status) {
        log.debug("REST request to update Feedback status : {}", id);
        Feedback result = feedbackService.updateStatus(id, status);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .body(result);
    }

    @GetMapping("/requester/{requesterPin}")
    public ResponseEntity<List<Feedback>> getFeedbackRequestsByRequester(@PathVariable String requesterPin) {
        log.debug("REST request to get Feedbacks for requester : {}", requesterPin);
        List<Feedback> result = feedbackService.getFeedbackRequestsByRequester(requesterPin);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employees/eligible")
    public ResponseEntity<List<Employee>> getEligibleEmployees(
        @RequestParam ResponderCategory category,
        @RequestParam String currentUserPin
    ) {
        log.debug("REST request to get eligible employees for category : {}, currentUserPin : {}", category, currentUserPin);
        List<Employee> result = employeeService.getEligibleEmployees(category, currentUserPin);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/employees/search")
    public ResponseEntity<List<Employee>> searchEmployees(
        @RequestParam String term,
        @RequestParam ResponderCategory category,
        @RequestParam String currentUserPin
    ) {
        log.debug("REST request to search employees for term : {}, category : {}, currentUserPin : {}", term, category, currentUserPin);
        List<Employee> result = employeeService.searchEmployeess(term, category, currentUserPin);
        return ResponseEntity.ok().body(result);
    }

    /**
     * {@code PUT  /feedbacks/:id} : Updates an existing feedback.
     *
     * @param id the id of the feedback to save.
     * @param feedback the feedback to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedback,
     * or with status {@code 400 (Bad Request)} if the feedback is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feedback couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Feedback feedback
    ) throws URISyntaxException {
        log.debug("REST request to update Feedback : {}, {}", id, feedback);
        if (feedback.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedback.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feedback = feedbackService.update(feedback);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedback.getId().toString()))
            .body(feedback);
    }

    /**
     * {@code PATCH  /feedbacks/:id} : Partial updates given fields of an existing feedback, field will ignore if it is null
     *
     * @param id the id of the feedback to save.
     * @param feedback the feedback to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feedback,
     * or with status {@code 400 (Bad Request)} if the feedback is not valid,
     * or with status {@code 404 (Not Found)} if the feedback is not found,
     * or with status {@code 500 (Internal Server Error)} if the feedback couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Feedback> partialUpdateFeedback(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Feedback feedback
    ) throws URISyntaxException {
        log.debug("REST request to partial update Feedback partially : {}, {}", id, feedback);
        if (feedback.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feedback.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feedbackRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Feedback> result = feedbackService.partialUpdate(feedback);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feedback.getId().toString())
        );
    }

    /**
     * {@code GET  /feedbacks} : get all the feedbacks.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbacks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Feedback>> getAllFeedbacks(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Feedbacks");
        Page<Feedback> page;
        if (eagerload) {
            page = feedbackService.findAllWithEagerRelationships(pageable);
        } else {
            page = feedbackService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feedbacks/:id} : get the "id" feedback.
     *
     * @param id the id of the feedback to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedback, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedback(@PathVariable("id") Long id) {
        log.debug("REST request to get Feedback : {}", id);
        Optional<Feedback> feedback = feedbackService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feedback);
    }

    /**
     * {@code DELETE  /feedbacks/:id} : delete the "id" feedback.
     *
     * @param id the id of the feedback to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable("id") Long id) {
        log.debug("REST request to delete Feedback : {}", id);
        feedbackService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
