import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../skill-development-details.test-samples';

import { SkillDevelopmentDetailsFormService } from './skill-development-details-form.service';

describe('SkillDevelopmentDetails Form Service', () => {
  let service: SkillDevelopmentDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SkillDevelopmentDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createSkillDevelopmentDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSkillDevelopmentDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skillDevelopmentType: expect.any(Object),
            responder: expect.any(Object),
          }),
        );
      });

      it('passing ISkillDevelopmentDetails should create a new form with FormGroup', () => {
        const formGroup = service.createSkillDevelopmentDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skillDevelopmentType: expect.any(Object),
            responder: expect.any(Object),
          }),
        );
      });
    });

    describe('getSkillDevelopmentDetails', () => {
      it('should return NewSkillDevelopmentDetails for default SkillDevelopmentDetails initial value', () => {
        const formGroup = service.createSkillDevelopmentDetailsFormGroup(sampleWithNewData);

        const skillDevelopmentDetails = service.getSkillDevelopmentDetails(formGroup) as any;

        expect(skillDevelopmentDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewSkillDevelopmentDetails for empty SkillDevelopmentDetails initial value', () => {
        const formGroup = service.createSkillDevelopmentDetailsFormGroup();

        const skillDevelopmentDetails = service.getSkillDevelopmentDetails(formGroup) as any;

        expect(skillDevelopmentDetails).toMatchObject({});
      });

      it('should return ISkillDevelopmentDetails', () => {
        const formGroup = service.createSkillDevelopmentDetailsFormGroup(sampleWithRequiredData);

        const skillDevelopmentDetails = service.getSkillDevelopmentDetails(formGroup) as any;

        expect(skillDevelopmentDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISkillDevelopmentDetails should not enable id FormControl', () => {
        const formGroup = service.createSkillDevelopmentDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSkillDevelopmentDetails should disable id FormControl', () => {
        const formGroup = service.createSkillDevelopmentDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
