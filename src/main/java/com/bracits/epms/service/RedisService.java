package com.bracits.epms.service;

public interface RedisService {
    void saveFeedbackProgress(String userPin, int year, Object progressData);
    void saveFeedbackAssessmentProgress(Long responderId, Object progressData);
    Object loadFeedbackProgress(String userPin, int year);
    Object loadFeedbackAssessmentProgress(Long responderId);
    void deleteFeedbackAssessment(Long responderId);
    void deleteFeedbackProgress(String userPin, int year);
}
