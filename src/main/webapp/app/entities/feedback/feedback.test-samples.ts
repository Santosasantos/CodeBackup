import dayjs from 'dayjs/esm';

import { IFeedback, NewFeedback } from './feedback.model';

export const sampleWithRequiredData: IFeedback = {
  id: 19963,
  requestDate: dayjs('2024-09-18T15:25'),
  status: 'SAVE_AS_DRAFT',
  createdBy: 'cooperative',
  assessmentYear: 8378,
};

export const sampleWithPartialData: IFeedback = {
  id: 29357,
  requestDate: dayjs('2024-09-18T20:03'),
  status: 'REJECTED',
  responseDate: dayjs('2024-09-19'),
  createdBy: 'take bank yieldingly',
  assessmentYear: 4210,
};

export const sampleWithFullData: IFeedback = {
  id: 30760,
  requestDate: dayjs('2024-09-19T01:46'),
  status: 'REJECTED',
  responseDate: dayjs('2024-09-19'),
  createdBy: 'drat honestly',
  assessmentYear: 15378,
};

export const sampleWithNewData: NewFeedback = {
  requestDate: dayjs('2024-09-19T06:03'),
  status: 'SAVE_AS_DRAFT',
  createdBy: 'beside',
  assessmentYear: 30877,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
