package com.bracits.epms.service.impl;

import com.bracits.epms.service.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveFeedbackProgress(String userPin, int year, Object progressData) {
        String key = userPin + "_feedbacklist_" + year;
        redisTemplate.opsForValue().set(key, progressData);
    }

    @Override
    public void saveFeedbackAssessmentProgress(Long responderId, Object progressData) {
        Object assessmentProgress = loadFeedbackAssessmentProgress(responderId);
        if (assessmentProgress != null) {
            deleteFeedbackAssessment(responderId);
        }
        String key = "feedback_assessment_for_" + responderId;
        redisTemplate.opsForValue().set(key, progressData);
    }

    @Override
    public Object loadFeedbackProgress(String userPin, int year) {
        String key = userPin + "_feedbacklist_" + year;
        return (Object) redisTemplate.opsForValue().get(key);
    }

    @Override
    public Object loadFeedbackAssessmentProgress(Long responderId) {
        String key = "feedback_assessment_for_" + responderId;
        return (Object) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void deleteFeedbackAssessment(Long responderId) {
        String key = "feedback_assessment_for_" + responderId;
        redisTemplate.delete(key);
    }

    @Override
    public void deleteFeedbackProgress(String userPin, int year) {
        String key = userPin + "_feedbacklist_" + year;
        redisTemplate.delete(key);
    }
}
