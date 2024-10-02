import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-type.test-samples';

import { FeedbackTypeFormService } from './feedback-type-form.service';

describe('FeedbackType Form Service', () => {
  let service: FeedbackTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackTypeFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedbackname: expect.any(Object),
          }),
        );
      });

      it('passing IFeedbackType should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedbackname: expect.any(Object),
          }),
        );
      });
    });

    describe('getFeedbackType', () => {
      it('should return NewFeedbackType for default FeedbackType initial value', () => {
        const formGroup = service.createFeedbackTypeFormGroup(sampleWithNewData);

        const feedbackType = service.getFeedbackType(formGroup) as any;

        expect(feedbackType).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackType for empty FeedbackType initial value', () => {
        const formGroup = service.createFeedbackTypeFormGroup();

        const feedbackType = service.getFeedbackType(formGroup) as any;

        expect(feedbackType).toMatchObject({});
      });

      it('should return IFeedbackType', () => {
        const formGroup = service.createFeedbackTypeFormGroup(sampleWithRequiredData);

        const feedbackType = service.getFeedbackType(formGroup) as any;

        expect(feedbackType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackType should not enable id FormControl', () => {
        const formGroup = service.createFeedbackTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackType should disable id FormControl', () => {
        const formGroup = service.createFeedbackTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
