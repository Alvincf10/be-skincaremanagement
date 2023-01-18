import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIngridient } from '../ingridient.model';
import { IngridientService } from '../service/ingridient.service';

@Injectable({ providedIn: 'root' })
export class IngridientRoutingResolveService implements Resolve<IIngridient | null> {
  constructor(protected service: IngridientService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIngridient | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ingridient: HttpResponse<IIngridient>) => {
          if (ingridient.body) {
            return of(ingridient.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
