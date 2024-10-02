import { IExtraquestionAns, NewExtraquestionAns } from './extraquestion-ans.model';

export const sampleWithRequiredData: IExtraquestionAns = {
  id: 20811,
  questionans: 'automatic gah droopy',
};

export const sampleWithPartialData: IExtraquestionAns = {
  id: 28830,
  questionans: 'indeed oh',
};

export const sampleWithFullData: IExtraquestionAns = {
  id: 19461,
  questionans: 'why purple staid',
};

export const sampleWithNewData: NewExtraquestionAns = {
  questionans: 'unless',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
