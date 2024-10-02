import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../teach-other.test-samples';

import { TeachOtherFormService } from './teach-other-form.service';

describe('TeachOther Form Service', () => {
  let service: TeachOtherFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TeachOtherFormService);
  });

  describe('Service methods', () => {
    describe('createTeachOtherFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTeachOtherFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            technicalSkill: expect.any(Object),
            recommendation: expect.any(Object),
            particularStrengh: expect.any(Object),
            whynotRecommend: expect.any(Object),
            responder: expect.any(Object),
          }),
        );
      });

      it('passing ITeachOther should create a new form with FormGroup', () => {
        const formGroup = service.createTeachOtherFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            technicalSkill: expect.any(Object),
            recommendation: expect.any(Object),
            particularStrengh: expect.any(Object),
            whynotRecommend: expect.any(Object),
            responder: expect.any(Object),
          }),
        );
      });
    });

    describe('getTeachOther', () => {
      it('should return NewTeachOther for default TeachOther initial value', () => {
        const formGroup = service.createTeachOtherFormGroup(sampleWithNewData);

        const teachOther = service.getTeachOther(formGroup) as any;

        expect(teachOther).toMatchObject(sampleWithNewData);
      });

      it('should return NewTeachOther for empty TeachOther initial value', () => {
        const formGroup = service.createTeachOtherFormGroup();

        const teachOther = service.getTeachOther(formGroup) as any;

        expect(teachOther).toMatchObject({});
      });

      it('should return ITeachOther', () => {
        const formGroup = service.createTeachOtherFormGroup(sampleWithRequiredData);

        const teachOther = service.getTeachOther(formGroup) as any;

        expect(teachOther).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITeachOther should not enable id FormControl', () => {
        const formGroup = service.createTeachOtherFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTeachOther should disable id FormControl', () => {
        const formGroup = service.createTeachOtherFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
