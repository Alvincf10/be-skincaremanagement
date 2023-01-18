export interface IIngridient {
  id: number;
  ingridientName?: string | null;
}

export type NewIngridient = Omit<IIngridient, 'id'> & { id: null };
