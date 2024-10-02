import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-sub-type.test-samples';

import { FeedbackSubTypeFormService } from './feedback-sub-type-form.service';

describe('FeedbackSubType Form Service', () => {
  let service: FeedbackSubTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackSubTypeFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackSubTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackSubTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedbacksubname: expect.any(Object),
            feedbackdescription: expect.any(Object),
            feedbackType: expect.any(Object),
          }),
        );
      });

      it('passing IFeedbackSubType should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackSubTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            feedbacksubname: expect.any(Object),
            feedbackdescription: expect.any(Object),
            feedbackType: expect.any(Object),
          }),
        );
      });
    });

    describe('getFeedbackSubType', () => {
      it('should return NewFeedbackSubType for default FeedbackSubType initial value', () => {
        const formGroup = service.createFeedbackSubTypeFormGroup(sampleWithNewData);

        const feedbackSubType = service.getFeedbackSubType(formGroup) as any;

        expect(feedbackSubType).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackSubType for empty FeedbackSubType initial value', () => {
        const formGroup = service.createFeedbackSubTypeFormGroup();

        const feedbackSubType = service.getFeedbackSubType(formGroup) as any;

        expect(feedbackSubType).toMatchObject({});
      });

      it('should return IFeedbackSubType', () => {
        const formGroup = service.createFeedbackSubTypeFormGroup(sampleWithRequiredData);

        const feedbackSubType = service.getFeedbackSubType(formGroup) as any;

        expect(feedbackSubType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackSubType should not enable id FormControl', () => {
        const formGroup = service.createFeedbackSubTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackSubType should disable id FormControl', () => {
        const formGroup = service.createFeedbackSubTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
