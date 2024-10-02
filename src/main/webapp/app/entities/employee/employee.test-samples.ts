import dayjs from 'dayjs/esm';

import { IEmployee, NewEmployee } from './employee.model';

export const sampleWithRequiredData: IEmployee = {
  id: 842,
  firstname: 'who notebook',
  lastname: 'sort ick',
  pin: 'well-off',
  employeeCategory: 'UNKNOWN',
  jobStatus: 'INACTIVE',
  employeeStatus: 'CONFIRM',
  gender: 'FEMALE',
};

export const sampleWithPartialData: IEmployee = {
  id: 29081,
  firstname: 'dimly unacceptable u',
  lastname: 'helplessly taper',
  pin: 'needily ',
  project: 'excepting through craftsman',
  employeeCategory: 'CONTRACT',
  joiningDate: dayjs('2024-09-18T11:22'),
  currentOffice: 'despite ick iceberg',
  jobStatus: 'INACTIVE',
  employeeStatus: 'NONCONFIRM',
  gender: 'MALE',
  email: 'Jaden.Herzog89@yahoo.com',
  profile: '../fake-data/blob/hipster.png',
  profileContentType: 'unknown',
};

export const sampleWithFullData: IEmployee = {
  id: 25123,
  firstname: 'coruscate',
  lastname: 'composer monthly',
  pin: 'gosh ice',
  project: 'ill-informed how incidentally',
  employeeCategory: 'INTERN',
  designation: 'faithfully',
  functionalDesignation: 'ew to',
  joiningDate: dayjs('2024-09-19T01:47'),
  currentOffice: 'afterwards',
  jobStatus: 'ACTIVE',
  employeeStatus: 'CONFIRM',
  dateOfBirth: dayjs('2024-09-18'),
  gender: 'MALE',
  mobile: 'seldom',
  email: 'Marquise_Hintz96@hotmail.com',
  grade: 25622,
  profile: '../fake-data/blob/hipster.png',
  profileContentType: 'unknown',
};

export const sampleWithNewData: NewEmployee = {
  firstname: 'transact',
  lastname: 'possible out whoa',
  pin: 'amidst i',
  employeeCategory: 'REGULAR',
  jobStatus: 'ACTIVE',
  employeeStatus: 'CONFIRM',
  gender: 'OTHER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
