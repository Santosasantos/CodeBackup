import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-details.test-samples';

import { FeedbackDetailsFormService } from './feedback-details-form.service';

describe('FeedbackDetails Form Service', () => {
  let service: FeedbackDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            commentsforfeedbacksubtype: expect.any(Object),
            ratingvalue: expect.any(Object),
            feedbackSubType: expect.any(Object),
            responder: expect.any(Object),
          }),
        );
      });

      it('passing IFeedbackDetails should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            commentsforfeedbacksubtype: expect.any(Object),
            ratingvalue: expect.any(Object),
            feedbackSubType: expect.any(Object),
            responder: expect.any(Object),
          }),
        );
      });
    });

    describe('getFeedbackDetails', () => {
      it('should return NewFeedbackDetails for default FeedbackDetails initial value', () => {
        const formGroup = service.createFeedbackDetailsFormGroup(sampleWithNewData);

        const feedbackDetails = service.getFeedbackDetails(formGroup) as any;

        expect(feedbackDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackDetails for empty FeedbackDetails initial value', () => {
        const formGroup = service.createFeedbackDetailsFormGroup();

        const feedbackDetails = service.getFeedbackDetails(formGroup) as any;

        expect(feedbackDetails).toMatchObject({});
      });

      it('should return IFeedbackDetails', () => {
        const formGroup = service.createFeedbackDetailsFormGroup(sampleWithRequiredData);

        const feedbackDetails = service.getFeedbackDetails(formGroup) as any;

        expect(feedbackDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackDetails should not enable id FormControl', () => {
        const formGroup = service.createFeedbackDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackDetails should disable id FormControl', () => {
        const formGroup = service.createFeedbackDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
