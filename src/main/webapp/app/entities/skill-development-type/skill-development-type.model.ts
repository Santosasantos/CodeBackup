export interface ISkillDevelopmentType {
  id: number;
  skilldevelopmentname?: string | null;
}

export type NewSkillDevelopmentType = Omit<ISkillDevelopmentType, 'id'> & { id: null };
