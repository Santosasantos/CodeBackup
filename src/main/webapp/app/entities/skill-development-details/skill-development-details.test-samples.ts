import { ISkillDevelopmentDetails, NewSkillDevelopmentDetails } from './skill-development-details.model';

export const sampleWithRequiredData: ISkillDevelopmentDetails = {
  id: 28701,
};

export const sampleWithPartialData: ISkillDevelopmentDetails = {
  id: 11583,
};

export const sampleWithFullData: ISkillDevelopmentDetails = {
  id: 9030,
};

export const sampleWithNewData: NewSkillDevelopmentDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
