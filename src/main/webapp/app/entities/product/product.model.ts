import dayjs from 'dayjs/esm';
import { IStock } from 'app/entities/stock/stock.model';
import { ICategory } from 'app/entities/category/category.model';
import { IIngridient } from 'app/entities/ingridient/ingridient.model';

export interface IProduct {
  id: number;
  productName?: string | null;
  expiredProduct?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  stock?: Pick<IStock, 'id'> | null;
  category?: Pick<ICategory, 'id'> | null;
  ingridient?: Pick<IIngridient, 'id'> | null;
}

export type NewProduct = Omit<IProduct, 'id'> & { id: null };
