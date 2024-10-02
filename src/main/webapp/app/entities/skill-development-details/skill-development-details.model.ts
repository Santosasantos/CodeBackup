import { ISkillDevelopmentType } from 'app/entities/skill-development-type/skill-development-type.model';
import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';

export interface ISkillDevelopmentDetails {
  id: number;
  skillDevelopmentType?: ISkillDevelopmentType | null;
  responder?: IFeedbackResponder | null;
}

export type NewSkillDevelopmentDetails = Omit<ISkillDevelopmentDetails, 'id'> & { id: null };
