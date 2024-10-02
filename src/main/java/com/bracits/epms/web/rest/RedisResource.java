package com.bracits.epms.web.rest;

import com.bracits.epms.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drafts")
public class RedisResource {

    @Autowired
    private final RedisService redisService;

    public RedisResource(RedisService redisService) {
        this.redisService = redisService;
    }

    @PostMapping("/save-feedback-assement")
    public void saveFeedbackAssessmentDraft(@RequestParam Long responderId, @RequestBody Object draftData) {
        redisService.saveFeedbackAssessmentProgress(responderId, draftData);
    }

    @PostMapping("/save-feedback-progress")
    public void saveFeedbackProgressDraft(@RequestParam String pin, @RequestParam Integer year, @RequestBody Object draftData) {
        redisService.saveFeedbackProgress(pin, year, draftData);
    }

    @GetMapping("/get-feedback-assessment")
    public ResponseEntity<Object> loadFeedbackAssessmentDraft(@RequestParam Long responderId) {
        Object draft = redisService.loadFeedbackAssessmentProgress(responderId);
        if (draft != null) {
            return ResponseEntity.ok(draft);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-feedback-assessment")
    public void deleteDraft(@RequestParam Long responderId) {
        redisService.deleteFeedbackAssessment(responderId);
    }

    @DeleteMapping("/delete-feedback-progress")
    public void deleteFeedbackProgress(@RequestParam String userPin, @RequestParam int year) {
        redisService.deleteFeedbackProgress(userPin, year);
    }
}
