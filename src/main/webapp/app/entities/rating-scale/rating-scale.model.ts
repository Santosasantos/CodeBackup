export interface IRatingScale {
  id: number;
  scaletype?: string | null;
  ratingscales?: string | null;
}

export type NewRatingScale = Omit<IRatingScale, 'id'> & { id: null };
