import { IFeedback } from 'app/entities/feedback/feedback.model';

export interface IExtraquestion {
  id: number;
  question?: string | null;
  feedback?: IFeedback | null;
}

export type NewExtraquestion = Omit<IExtraquestion, 'id'> & { id: null };
