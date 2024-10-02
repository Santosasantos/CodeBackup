export interface IFeedbackType {
  id: number;
  feedbackname?: string | null;
}

export type NewFeedbackType = Omit<IFeedbackType, 'id'> & { id: null };
