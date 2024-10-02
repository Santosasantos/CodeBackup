import { IExtraquestion, NewExtraquestion } from './extraquestion.model';

export const sampleWithRequiredData: IExtraquestion = {
  id: 26072,
  question: 'honestly',
};

export const sampleWithPartialData: IExtraquestion = {
  id: 22998,
  question: 'reload',
};

export const sampleWithFullData: IExtraquestion = {
  id: 9218,
  question: 'margin',
};

export const sampleWithNewData: NewExtraquestion = {
  question: 'badly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
