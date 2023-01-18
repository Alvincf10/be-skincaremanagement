import { IStock, NewStock } from './stock.model';

export const sampleWithRequiredData: IStock = {
  id: 51685,
};

export const sampleWithPartialData: IStock = {
  id: 68529,
};

export const sampleWithFullData: IStock = {
  id: 64815,
  quantityStock: 84173,
};

export const sampleWithNewData: NewStock = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
