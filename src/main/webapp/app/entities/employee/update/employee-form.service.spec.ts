import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../employee.test-samples';

import { EmployeeFormService } from './employee-form.service';

describe('Employee Form Service', () => {
  let service: EmployeeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmployeeFormService);
  });

  describe('Service methods', () => {
    describe('createEmployeeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEmployeeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            pin: expect.any(Object),
            project: expect.any(Object),
            employeeCategory: expect.any(Object),
            designation: expect.any(Object),
            functionalDesignation: expect.any(Object),
            joiningDate: expect.any(Object),
            currentOffice: expect.any(Object),
            jobStatus: expect.any(Object),
            employeeStatus: expect.any(Object),
            dateOfBirth: expect.any(Object),
            gender: expect.any(Object),
            mobile: expect.any(Object),
            email: expect.any(Object),
            grade: expect.any(Object),
            profile: expect.any(Object),
            supervisor: expect.any(Object),
          }),
        );
      });

      it('passing IEmployee should create a new form with FormGroup', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            pin: expect.any(Object),
            project: expect.any(Object),
            employeeCategory: expect.any(Object),
            designation: expect.any(Object),
            functionalDesignation: expect.any(Object),
            joiningDate: expect.any(Object),
            currentOffice: expect.any(Object),
            jobStatus: expect.any(Object),
            employeeStatus: expect.any(Object),
            dateOfBirth: expect.any(Object),
            gender: expect.any(Object),
            mobile: expect.any(Object),
            email: expect.any(Object),
            grade: expect.any(Object),
            profile: expect.any(Object),
            supervisor: expect.any(Object),
          }),
        );
      });
    });

    describe('getEmployee', () => {
      it('should return NewEmployee for default Employee initial value', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithNewData);

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject(sampleWithNewData);
      });

      it('should return NewEmployee for empty Employee initial value', () => {
        const formGroup = service.createEmployeeFormGroup();

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject({});
      });

      it('should return IEmployee', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);

        const employee = service.getEmployee(formGroup) as any;

        expect(employee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEmployee should not enable id FormControl', () => {
        const formGroup = service.createEmployeeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEmployee should disable id FormControl', () => {
        const formGroup = service.createEmployeeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
