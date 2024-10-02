import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../skill-development-type.test-samples';

import { SkillDevelopmentTypeFormService } from './skill-development-type-form.service';

describe('SkillDevelopmentType Form Service', () => {
  let service: SkillDevelopmentTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SkillDevelopmentTypeFormService);
  });

  describe('Service methods', () => {
    describe('createSkillDevelopmentTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSkillDevelopmentTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skilldevelopmentname: expect.any(Object),
          }),
        );
      });

      it('passing ISkillDevelopmentType should create a new form with FormGroup', () => {
        const formGroup = service.createSkillDevelopmentTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            skilldevelopmentname: expect.any(Object),
          }),
        );
      });
    });

    describe('getSkillDevelopmentType', () => {
      it('should return NewSkillDevelopmentType for default SkillDevelopmentType initial value', () => {
        const formGroup = service.createSkillDevelopmentTypeFormGroup(sampleWithNewData);

        const skillDevelopmentType = service.getSkillDevelopmentType(formGroup) as any;

        expect(skillDevelopmentType).toMatchObject(sampleWithNewData);
      });

      it('should return NewSkillDevelopmentType for empty SkillDevelopmentType initial value', () => {
        const formGroup = service.createSkillDevelopmentTypeFormGroup();

        const skillDevelopmentType = service.getSkillDevelopmentType(formGroup) as any;

        expect(skillDevelopmentType).toMatchObject({});
      });

      it('should return ISkillDevelopmentType', () => {
        const formGroup = service.createSkillDevelopmentTypeFormGroup(sampleWithRequiredData);

        const skillDevelopmentType = service.getSkillDevelopmentType(formGroup) as any;

        expect(skillDevelopmentType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISkillDevelopmentType should not enable id FormControl', () => {
        const formGroup = service.createSkillDevelopmentTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSkillDevelopmentType should disable id FormControl', () => {
        const formGroup = service.createSkillDevelopmentTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
