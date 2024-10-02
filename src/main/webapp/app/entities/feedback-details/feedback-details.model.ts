import { IFeedbackSubType } from 'app/entities/feedback-sub-type/feedback-sub-type.model';
import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';

export interface IFeedbackDetails {
  id: number;
  commentsforfeedbacksubtype?: string | null;
  ratingvalue?: number | null;
  feedbackSubType?: IFeedbackSubType | null;
  responder?: IFeedbackResponder | null;
}

export type NewFeedbackDetails = Omit<IFeedbackDetails, 'id'> & { id: null };
