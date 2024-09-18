import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 16516,
  login: "LW@BpzQO\\lbrBJY\\%gYk\\'bUZe49\\=JSz\\Bz",
};

export const sampleWithPartialData: IUser = {
  id: 2605,
  login: '4J7d7',
};

export const sampleWithFullData: IUser = {
  id: 6224,
  login: 'pjQ',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
