import { IEmployee } from 'app/entities/employee/employee.model';
import { IFeedback } from 'app/entities/feedback/feedback.model';
import { ResponderCategory } from 'app/entities/enumerations/responder-category.model';
import { FeedbackStatus } from 'app/entities/enumerations/feedback-status.model';

export interface IFeedbackResponder {
  id: number;
  category?: keyof typeof ResponderCategory | null;
  stakeholderEmail?: string | null;
  responderStatus?: keyof typeof FeedbackStatus | null;
  employee?: IEmployee | null;
  feedback?: IFeedback | null;
}

export type NewFeedbackResponder = Omit<IFeedbackResponder, 'id'> & { id: null };
