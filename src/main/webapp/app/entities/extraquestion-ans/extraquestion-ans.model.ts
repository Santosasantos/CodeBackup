import { IExtraquestion } from 'app/entities/extraquestion/extraquestion.model';

export interface IExtraquestionAns {
  id: number;
  questionans?: string | null;
  questions?: IExtraquestion | null;
}

export type NewExtraquestionAns = Omit<IExtraquestionAns, 'id'> & { id: null };
