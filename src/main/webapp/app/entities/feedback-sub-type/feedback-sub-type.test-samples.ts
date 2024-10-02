import { IFeedbackSubType, NewFeedbackSubType } from './feedback-sub-type.model';

export const sampleWithRequiredData: IFeedbackSubType = {
  id: 9599,
  feedbacksubname: 'ripe unzip briskly',
  feedbackdescription: 'once snarling',
};

export const sampleWithPartialData: IFeedbackSubType = {
  id: 31645,
  feedbacksubname: 'rhyme',
  feedbackdescription: 'coordinated unless minus',
};

export const sampleWithFullData: IFeedbackSubType = {
  id: 3626,
  feedbacksubname: 'tout haemorrhage between',
  feedbackdescription: 'uselessly',
};

export const sampleWithNewData: NewFeedbackSubType = {
  feedbacksubname: 'happily',
  feedbackdescription: 'sleepy mineshaft',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
