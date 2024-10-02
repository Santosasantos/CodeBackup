import { IFeedbackResponder, NewFeedbackResponder } from './feedback-responder.model';

export const sampleWithRequiredData: IFeedbackResponder = {
  id: 26129,
  category: 'SUPERVISEE',
  responderStatus: 'NEW',
};

export const sampleWithPartialData: IFeedbackResponder = {
  id: 16285,
  category: 'STAKEHOLDER',
  stakeholderEmail: 'energetic psst',
  responderStatus: 'APPROVED',
};

export const sampleWithFullData: IFeedbackResponder = {
  id: 12537,
  category: 'SELF',
  stakeholderEmail: 'although comfortable abaft',
  responderStatus: 'SAVE_AS_DRAFT',
};

export const sampleWithNewData: NewFeedbackResponder = {
  category: 'PEER',
  responderStatus: 'APPROVED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
