export interface IStock {
  id: number;
  quantityStock?: number | null;
}

export type NewStock = Omit<IStock, 'id'> & { id: null };
