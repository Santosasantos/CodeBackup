import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IRatingScale } from 'app/entities/rating-scale/rating-scale.model';
import { FeedbackStatus } from 'app/entities/enumerations/feedback-status.model';

export interface IFeedback {
  id: number;
  requestDate?: dayjs.Dayjs | null;
  status?: keyof typeof FeedbackStatus | null;
  responseDate?: dayjs.Dayjs | null;
  createdBy?: string | null;
  assessmentYear?: number | null;
  requester?: IEmployee | null;
  ratingScale?: IRatingScale | null;
}

export type NewFeedback = Omit<IFeedback, 'id'> & { id: null };
