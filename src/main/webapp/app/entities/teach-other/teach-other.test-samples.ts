import { ITeachOther, NewTeachOther } from './teach-other.model';

export const sampleWithRequiredData: ITeachOther = {
  id: 25027,
  technicalSkill: 'apropos fatally',
  recommendation: 'No',
};

export const sampleWithPartialData: ITeachOther = {
  id: 21095,
  technicalSkill: 'unequaled',
  recommendation: 'No',
  particularStrengh: 'calculate capitulate',
};

export const sampleWithFullData: ITeachOther = {
  id: 25608,
  technicalSkill: 'likeness ballot upon',
  recommendation: 'Yes',
  particularStrengh: 'height',
  whynotRecommend: 'collocate far-flung octagon',
};

export const sampleWithNewData: NewTeachOther = {
  technicalSkill: 'during',
  recommendation: 'Yes',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
