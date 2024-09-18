import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'ceb3873b-33c4-4e3c-a8c2-bc7e9eb27899',
};

export const sampleWithPartialData: IAuthority = {
  name: 'efc3af17-0fa7-4fcc-ac48-0a8cd9175996',
};

export const sampleWithFullData: IAuthority = {
  name: '66069990-fffd-46d1-b6a3-88e2ba160999',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
