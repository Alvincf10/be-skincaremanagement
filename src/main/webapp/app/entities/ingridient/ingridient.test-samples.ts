import { IIngridient, NewIngridient } from './ingridient.model';

export const sampleWithRequiredData: IIngridient = {
  id: 35769,
};

export const sampleWithPartialData: IIngridient = {
  id: 91421,
};

export const sampleWithFullData: IIngridient = {
  id: 36685,
  ingridientName: 'Future database',
};

export const sampleWithNewData: NewIngridient = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
