import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../extraquestion-ans.test-samples';

import { ExtraquestionAnsFormService } from './extraquestion-ans-form.service';

describe('ExtraquestionAns Form Service', () => {
  let service: ExtraquestionAnsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExtraquestionAnsFormService);
  });

  describe('Service methods', () => {
    describe('createExtraquestionAnsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExtraquestionAnsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            questionans: expect.any(Object),
            questions: expect.any(Object),
          }),
        );
      });

      it('passing IExtraquestionAns should create a new form with FormGroup', () => {
        const formGroup = service.createExtraquestionAnsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            questionans: expect.any(Object),
            questions: expect.any(Object),
          }),
        );
      });
    });

    describe('getExtraquestionAns', () => {
      it('should return NewExtraquestionAns for default ExtraquestionAns initial value', () => {
        const formGroup = service.createExtraquestionAnsFormGroup(sampleWithNewData);

        const extraquestionAns = service.getExtraquestionAns(formGroup) as any;

        expect(extraquestionAns).toMatchObject(sampleWithNewData);
      });

      it('should return NewExtraquestionAns for empty ExtraquestionAns initial value', () => {
        const formGroup = service.createExtraquestionAnsFormGroup();

        const extraquestionAns = service.getExtraquestionAns(formGroup) as any;

        expect(extraquestionAns).toMatchObject({});
      });

      it('should return IExtraquestionAns', () => {
        const formGroup = service.createExtraquestionAnsFormGroup(sampleWithRequiredData);

        const extraquestionAns = service.getExtraquestionAns(formGroup) as any;

        expect(extraquestionAns).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExtraquestionAns should not enable id FormControl', () => {
        const formGroup = service.createExtraquestionAnsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExtraquestionAns should disable id FormControl', () => {
        const formGroup = service.createExtraquestionAnsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
