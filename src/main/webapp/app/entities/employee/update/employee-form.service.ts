import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEmployee, NewEmployee } from '../employee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEmployee for edit and NewEmployeeFormGroupInput for create.
 */
type EmployeeFormGroupInput = IEmployee | PartialWithRequiredKeyOf<NewEmployee>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEmployee | NewEmployee> = Omit<T, 'joiningDate'> & {
  joiningDate?: string | null;
};

type EmployeeFormRawValue = FormValueOf<IEmployee>;

type NewEmployeeFormRawValue = FormValueOf<NewEmployee>;

type EmployeeFormDefaults = Pick<NewEmployee, 'id' | 'joiningDate'>;

type EmployeeFormGroupContent = {
  id: FormControl<EmployeeFormRawValue['id'] | NewEmployee['id']>;
  firstname: FormControl<EmployeeFormRawValue['firstname']>;
  lastname: FormControl<EmployeeFormRawValue['lastname']>;
  pin: FormControl<EmployeeFormRawValue['pin']>;
  project: FormControl<EmployeeFormRawValue['project']>;
  employeeCategory: FormControl<EmployeeFormRawValue['employeeCategory']>;
  designation: FormControl<EmployeeFormRawValue['designation']>;
  functionalDesignation: FormControl<EmployeeFormRawValue['functionalDesignation']>;
  joiningDate: FormControl<EmployeeFormRawValue['joiningDate']>;
  currentOffice: FormControl<EmployeeFormRawValue['currentOffice']>;
  jobStatus: FormControl<EmployeeFormRawValue['jobStatus']>;
  employeeStatus: FormControl<EmployeeFormRawValue['employeeStatus']>;
  dateOfBirth: FormControl<EmployeeFormRawValue['dateOfBirth']>;
  gender: FormControl<EmployeeFormRawValue['gender']>;
  mobile: FormControl<EmployeeFormRawValue['mobile']>;
  email: FormControl<EmployeeFormRawValue['email']>;
  grade: FormControl<EmployeeFormRawValue['grade']>;
  profile: FormControl<EmployeeFormRawValue['profile']>;
  profileContentType: FormControl<EmployeeFormRawValue['profileContentType']>;
  supervisor: FormControl<EmployeeFormRawValue['supervisor']>;
};

export type EmployeeFormGroup = FormGroup<EmployeeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EmployeeFormService {
  createEmployeeFormGroup(employee: EmployeeFormGroupInput = { id: null }): EmployeeFormGroup {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({
      ...this.getFormDefaults(),
      ...employee,
    });
    return new FormGroup<EmployeeFormGroupContent>({
      id: new FormControl(
        { value: employeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstname: new FormControl(employeeRawValue.firstname, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(20)],
      }),
      lastname: new FormControl(employeeRawValue.lastname, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(20)],
      }),
      pin: new FormControl(employeeRawValue.pin, {
        validators: [Validators.required, Validators.minLength(4), Validators.maxLength(8)],
      }),
      project: new FormControl(employeeRawValue.project),
      employeeCategory: new FormControl(employeeRawValue.employeeCategory, {
        validators: [Validators.required],
      }),
      designation: new FormControl(employeeRawValue.designation),
      functionalDesignation: new FormControl(employeeRawValue.functionalDesignation),
      joiningDate: new FormControl(employeeRawValue.joiningDate),
      currentOffice: new FormControl(employeeRawValue.currentOffice),
      jobStatus: new FormControl(employeeRawValue.jobStatus, {
        validators: [Validators.required],
      }),
      employeeStatus: new FormControl(employeeRawValue.employeeStatus, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(employeeRawValue.dateOfBirth),
      gender: new FormControl(employeeRawValue.gender, {
        validators: [Validators.required],
      }),
      mobile: new FormControl(employeeRawValue.mobile),
      email: new FormControl(employeeRawValue.email),
      grade: new FormControl(employeeRawValue.grade),
      profile: new FormControl(employeeRawValue.profile),
      profileContentType: new FormControl(employeeRawValue.profileContentType),
      supervisor: new FormControl(employeeRawValue.supervisor),
    });
  }

  getEmployee(form: EmployeeFormGroup): IEmployee | NewEmployee {
    return this.convertEmployeeRawValueToEmployee(form.getRawValue() as EmployeeFormRawValue | NewEmployeeFormRawValue);
  }

  resetForm(form: EmployeeFormGroup, employee: EmployeeFormGroupInput): void {
    const employeeRawValue = this.convertEmployeeToEmployeeRawValue({ ...this.getFormDefaults(), ...employee });
    form.reset(
      {
        ...employeeRawValue,
        id: { value: employeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EmployeeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      joiningDate: currentTime,
    };
  }

  private convertEmployeeRawValueToEmployee(rawEmployee: EmployeeFormRawValue | NewEmployeeFormRawValue): IEmployee | NewEmployee {
    return {
      ...rawEmployee,
      joiningDate: dayjs(rawEmployee.joiningDate, DATE_TIME_FORMAT),
    };
  }

  private convertEmployeeToEmployeeRawValue(
    employee: IEmployee | (Partial<NewEmployee> & EmployeeFormDefaults),
  ): EmployeeFormRawValue | PartialWithRequiredKeyOf<NewEmployeeFormRawValue> {
    return {
      ...employee,
      joiningDate: employee.joiningDate ? employee.joiningDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
