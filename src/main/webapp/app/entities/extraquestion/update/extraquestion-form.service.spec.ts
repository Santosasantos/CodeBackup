import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../extraquestion.test-samples';

import { ExtraquestionFormService } from './extraquestion-form.service';

describe('Extraquestion Form Service', () => {
  let service: ExtraquestionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExtraquestionFormService);
  });

  describe('Service methods', () => {
    describe('createExtraquestionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createExtraquestionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            question: expect.any(Object),
            feedback: expect.any(Object),
          }),
        );
      });

      it('passing IExtraquestion should create a new form with FormGroup', () => {
        const formGroup = service.createExtraquestionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            question: expect.any(Object),
            feedback: expect.any(Object),
          }),
        );
      });
    });

    describe('getExtraquestion', () => {
      it('should return NewExtraquestion for default Extraquestion initial value', () => {
        const formGroup = service.createExtraquestionFormGroup(sampleWithNewData);

        const extraquestion = service.getExtraquestion(formGroup) as any;

        expect(extraquestion).toMatchObject(sampleWithNewData);
      });

      it('should return NewExtraquestion for empty Extraquestion initial value', () => {
        const formGroup = service.createExtraquestionFormGroup();

        const extraquestion = service.getExtraquestion(formGroup) as any;

        expect(extraquestion).toMatchObject({});
      });

      it('should return IExtraquestion', () => {
        const formGroup = service.createExtraquestionFormGroup(sampleWithRequiredData);

        const extraquestion = service.getExtraquestion(formGroup) as any;

        expect(extraquestion).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IExtraquestion should not enable id FormControl', () => {
        const formGroup = service.createExtraquestionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewExtraquestion should disable id FormControl', () => {
        const formGroup = service.createExtraquestionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
