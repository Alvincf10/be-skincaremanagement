import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IngridientComponent } from '../list/ingridient.component';
import { IngridientDetailComponent } from '../detail/ingridient-detail.component';
import { IngridientUpdateComponent } from '../update/ingridient-update.component';
import { IngridientRoutingResolveService } from './ingridient-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ingridientRoute: Routes = [
  {
    path: '',
    component: IngridientComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IngridientDetailComponent,
    resolve: {
      ingridient: IngridientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IngridientUpdateComponent,
    resolve: {
      ingridient: IngridientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IngridientUpdateComponent,
    resolve: {
      ingridient: IngridientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ingridientRoute)],
  exports: [RouterModule],
})
export class IngridientRoutingModule {}
