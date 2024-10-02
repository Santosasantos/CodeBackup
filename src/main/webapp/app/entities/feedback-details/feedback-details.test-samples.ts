import { IFeedbackDetails, NewFeedbackDetails } from './feedback-details.model';

export const sampleWithRequiredData: IFeedbackDetails = {
  id: 3069,
};

export const sampleWithPartialData: IFeedbackDetails = {
  id: 15035,
  commentsforfeedbacksubtype: 'north yearly',
};

export const sampleWithFullData: IFeedbackDetails = {
  id: 22978,
  commentsforfeedbacksubtype: 'ew finally',
  ratingvalue: 1214,
};

export const sampleWithNewData: NewFeedbackDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
