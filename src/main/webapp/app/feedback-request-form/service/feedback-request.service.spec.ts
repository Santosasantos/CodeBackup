import { TestBed } from '@angular/core/testing';

import { FeedbackRequestService } from './feedback-request.service';

describe('FeedbackRequestService', () => {
  let service: FeedbackRequestService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackRequestService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
