import { TestBed } from '@angular/core/testing';

import { FeedbackReviewService } from './feedback-review.service';

describe('FeedbackReviewService', () => {
  let service: FeedbackReviewService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackReviewService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
