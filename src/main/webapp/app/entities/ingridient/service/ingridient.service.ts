import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIngridient, NewIngridient } from '../ingridient.model';

export type PartialUpdateIngridient = Partial<IIngridient> & Pick<IIngridient, 'id'>;

export type EntityResponseType = HttpResponse<IIngridient>;
export type EntityArrayResponseType = HttpResponse<IIngridient[]>;

@Injectable({ providedIn: 'root' })
export class IngridientService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ingridients');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ingridient: NewIngridient): Observable<EntityResponseType> {
    return this.http.post<IIngridient>(this.resourceUrl, ingridient, { observe: 'response' });
  }

  update(ingridient: IIngridient): Observable<EntityResponseType> {
    return this.http.put<IIngridient>(`${this.resourceUrl}/${this.getIngridientIdentifier(ingridient)}`, ingridient, {
      observe: 'response',
    });
  }

  partialUpdate(ingridient: PartialUpdateIngridient): Observable<EntityResponseType> {
    return this.http.patch<IIngridient>(`${this.resourceUrl}/${this.getIngridientIdentifier(ingridient)}`, ingridient, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IIngridient>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IIngridient[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getIngridientIdentifier(ingridient: Pick<IIngridient, 'id'>): number {
    return ingridient.id;
  }

  compareIngridient(o1: Pick<IIngridient, 'id'> | null, o2: Pick<IIngridient, 'id'> | null): boolean {
    return o1 && o2 ? this.getIngridientIdentifier(o1) === this.getIngridientIdentifier(o2) : o1 === o2;
  }

  addIngridientToCollectionIfMissing<Type extends Pick<IIngridient, 'id'>>(
    ingridientCollection: Type[],
    ...ingridientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ingridients: Type[] = ingridientsToCheck.filter(isPresent);
    if (ingridients.length > 0) {
      const ingridientCollectionIdentifiers = ingridientCollection.map(ingridientItem => this.getIngridientIdentifier(ingridientItem)!);
      const ingridientsToAdd = ingridients.filter(ingridientItem => {
        const ingridientIdentifier = this.getIngridientIdentifier(ingridientItem);
        if (ingridientCollectionIdentifiers.includes(ingridientIdentifier)) {
          return false;
        }
        ingridientCollectionIdentifiers.push(ingridientIdentifier);
        return true;
      });
      return [...ingridientsToAdd, ...ingridientCollection];
    }
    return ingridientCollection;
  }
}
