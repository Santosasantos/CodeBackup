import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../feedback-responder.test-samples';

import { FeedbackResponderFormService } from './feedback-responder-form.service';

describe('FeedbackResponder Form Service', () => {
  let service: FeedbackResponderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedbackResponderFormService);
  });

  describe('Service methods', () => {
    describe('createFeedbackResponderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createFeedbackResponderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            category: expect.any(Object),
            stakeholderEmail: expect.any(Object),
            responderStatus: expect.any(Object),
            employee: expect.any(Object),
            feedback: expect.any(Object),
          }),
        );
      });

      it('passing IFeedbackResponder should create a new form with FormGroup', () => {
        const formGroup = service.createFeedbackResponderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            category: expect.any(Object),
            stakeholderEmail: expect.any(Object),
            responderStatus: expect.any(Object),
            employee: expect.any(Object),
            feedback: expect.any(Object),
          }),
        );
      });
    });

    describe('getFeedbackResponder', () => {
      it('should return NewFeedbackResponder for default FeedbackResponder initial value', () => {
        const formGroup = service.createFeedbackResponderFormGroup(sampleWithNewData);

        const feedbackResponder = service.getFeedbackResponder(formGroup) as any;

        expect(feedbackResponder).toMatchObject(sampleWithNewData);
      });

      it('should return NewFeedbackResponder for empty FeedbackResponder initial value', () => {
        const formGroup = service.createFeedbackResponderFormGroup();

        const feedbackResponder = service.getFeedbackResponder(formGroup) as any;

        expect(feedbackResponder).toMatchObject({});
      });

      it('should return IFeedbackResponder', () => {
        const formGroup = service.createFeedbackResponderFormGroup(sampleWithRequiredData);

        const feedbackResponder = service.getFeedbackResponder(formGroup) as any;

        expect(feedbackResponder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IFeedbackResponder should not enable id FormControl', () => {
        const formGroup = service.createFeedbackResponderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewFeedbackResponder should disable id FormControl', () => {
        const formGroup = service.createFeedbackResponderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
