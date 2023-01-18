import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStock, NewStock } from '../stock.model';

export type PartialUpdateStock = Partial<IStock> & Pick<IStock, 'id'>;

export type EntityResponseType = HttpResponse<IStock>;
export type EntityArrayResponseType = HttpResponse<IStock[]>;

@Injectable({ providedIn: 'root' })
export class StockService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stocks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(stock: NewStock): Observable<EntityResponseType> {
    return this.http.post<IStock>(this.resourceUrl, stock, { observe: 'response' });
  }

  update(stock: IStock): Observable<EntityResponseType> {
    return this.http.put<IStock>(`${this.resourceUrl}/${this.getStockIdentifier(stock)}`, stock, { observe: 'response' });
  }

  partialUpdate(stock: PartialUpdateStock): Observable<EntityResponseType> {
    return this.http.patch<IStock>(`${this.resourceUrl}/${this.getStockIdentifier(stock)}`, stock, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStock>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStock[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStockIdentifier(stock: Pick<IStock, 'id'>): number {
    return stock.id;
  }

  compareStock(o1: Pick<IStock, 'id'> | null, o2: Pick<IStock, 'id'> | null): boolean {
    return o1 && o2 ? this.getStockIdentifier(o1) === this.getStockIdentifier(o2) : o1 === o2;
  }

  addStockToCollectionIfMissing<Type extends Pick<IStock, 'id'>>(
    stockCollection: Type[],
    ...stocksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stocks: Type[] = stocksToCheck.filter(isPresent);
    if (stocks.length > 0) {
      const stockCollectionIdentifiers = stockCollection.map(stockItem => this.getStockIdentifier(stockItem)!);
      const stocksToAdd = stocks.filter(stockItem => {
        const stockIdentifier = this.getStockIdentifier(stockItem);
        if (stockCollectionIdentifiers.includes(stockIdentifier)) {
          return false;
        }
        stockCollectionIdentifiers.push(stockIdentifier);
        return true;
      });
      return [...stocksToAdd, ...stockCollection];
    }
    return stockCollection;
  }
}
