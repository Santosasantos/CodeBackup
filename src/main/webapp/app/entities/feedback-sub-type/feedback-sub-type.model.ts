import { IFeedbackType } from 'app/entities/feedback-type/feedback-type.model';

export interface IFeedbackSubType {
  id: number;
  feedbacksubname?: string | null;
  feedbackdescription?: string | null;
  feedbackType?: IFeedbackType | null;
}

export type NewFeedbackSubType = Omit<IFeedbackSubType, 'id'> & { id: null };
