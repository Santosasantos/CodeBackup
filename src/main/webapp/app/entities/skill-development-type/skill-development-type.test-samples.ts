import { ISkillDevelopmentType, NewSkillDevelopmentType } from './skill-development-type.model';

export const sampleWithRequiredData: ISkillDevelopmentType = {
  id: 2526,
  skilldevelopmentname: 'rightfully tabletop massive',
};

export const sampleWithPartialData: ISkillDevelopmentType = {
  id: 23242,
  skilldevelopmentname: 'descent pessimistic drop',
};

export const sampleWithFullData: ISkillDevelopmentType = {
  id: 23434,
  skilldevelopmentname: 'ill-fated ick',
};

export const sampleWithNewData: NewSkillDevelopmentType = {
  skilldevelopmentname: 'pry never',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
