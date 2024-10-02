import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../rating-scale.test-samples';

import { RatingScaleFormService } from './rating-scale-form.service';

describe('RatingScale Form Service', () => {
  let service: RatingScaleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RatingScaleFormService);
  });

  describe('Service methods', () => {
    describe('createRatingScaleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRatingScaleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            scaletype: expect.any(Object),
            ratingscales: expect.any(Object),
          }),
        );
      });

      it('passing IRatingScale should create a new form with FormGroup', () => {
        const formGroup = service.createRatingScaleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            scaletype: expect.any(Object),
            ratingscales: expect.any(Object),
          }),
        );
      });
    });

    describe('getRatingScale', () => {
      it('should return NewRatingScale for default RatingScale initial value', () => {
        const formGroup = service.createRatingScaleFormGroup(sampleWithNewData);

        const ratingScale = service.getRatingScale(formGroup) as any;

        expect(ratingScale).toMatchObject(sampleWithNewData);
      });

      it('should return NewRatingScale for empty RatingScale initial value', () => {
        const formGroup = service.createRatingScaleFormGroup();

        const ratingScale = service.getRatingScale(formGroup) as any;

        expect(ratingScale).toMatchObject({});
      });

      it('should return IRatingScale', () => {
        const formGroup = service.createRatingScaleFormGroup(sampleWithRequiredData);

        const ratingScale = service.getRatingScale(formGroup) as any;

        expect(ratingScale).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRatingScale should not enable id FormControl', () => {
        const formGroup = service.createRatingScaleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRatingScale should disable id FormControl', () => {
        const formGroup = service.createRatingScaleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
