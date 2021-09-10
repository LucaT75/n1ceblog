import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpertComponent } from '../list/expert.component';
import { ExpertDetailComponent } from '../detail/expert-detail.component';
import { ExpertUpdateComponent } from '../update/expert-update.component';
import { ExpertRoutingResolveService } from './expert-routing-resolve.service';

const expertRoute: Routes = [
  {
    path: '',
    component: ExpertComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpertDetailComponent,
    resolve: {
      expert: ExpertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpertUpdateComponent,
    resolve: {
      expert: ExpertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpertUpdateComponent,
    resolve: {
      expert: ExpertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expertRoute)],
  exports: [RouterModule],
})
export class ExpertRoutingModule {}
