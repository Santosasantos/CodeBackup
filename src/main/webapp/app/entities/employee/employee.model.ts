import dayjs from 'dayjs/esm';
import { EmployeeCategory } from 'app/entities/enumerations/employee-category.model';
import { JobStatus } from 'app/entities/enumerations/job-status.model';
import { EmployeeStatus } from 'app/entities/enumerations/employee-status.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IEmployee {
  id: number;
  firstname?: string | null;
  lastname?: string | null;
  pin?: string | null;
  project?: string | null;
  employeeCategory?: keyof typeof EmployeeCategory | null;
  designation?: string | null;
  functionalDesignation?: string | null;
  joiningDate?: dayjs.Dayjs | null;
  currentOffice?: string | null;
  jobStatus?: keyof typeof JobStatus | null;
  employeeStatus?: keyof typeof EmployeeStatus | null;
  dateOfBirth?: dayjs.Dayjs | null;
  gender?: keyof typeof Gender | null;
  mobile?: string | null;
  email?: string | null;
  grade?: number | null;
  profile?: string | null;
  profileContentType?: string | null;
  supervisor?: IEmployee | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
