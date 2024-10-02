import { IRatingScale, NewRatingScale } from './rating-scale.model';

export const sampleWithRequiredData: IRatingScale = {
  id: 12869,
  scaletype: 'defile underneath frank',
  ratingscales: 'comfortable wholly novel',
};

export const sampleWithPartialData: IRatingScale = {
  id: 21216,
  scaletype: 'fooey lustrous',
  ratingscales: 'made-up versus',
};

export const sampleWithFullData: IRatingScale = {
  id: 22379,
  scaletype: 'connect afterwards junior',
  ratingscales: 'best lighting bah',
};

export const sampleWithNewData: NewRatingScale = {
  scaletype: 'instead above well',
  ratingscales: 'unbend',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
