import { IFeedbackType, NewFeedbackType } from './feedback-type.model';

export const sampleWithRequiredData: IFeedbackType = {
  id: 17654,
  feedbackname: 'yahoo joint pro',
};

export const sampleWithPartialData: IFeedbackType = {
  id: 31451,
  feedbackname: 'pish clobber',
};

export const sampleWithFullData: IFeedbackType = {
  id: 26837,
  feedbackname: 'till ew herald',
};

export const sampleWithNewData: NewFeedbackType = {
  feedbackname: 'honest pink',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
