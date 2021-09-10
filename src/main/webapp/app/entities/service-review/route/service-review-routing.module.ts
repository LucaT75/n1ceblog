import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServiceReviewComponent } from '../list/service-review.component';
import { ServiceReviewDetailComponent } from '../detail/service-review-detail.component';
import { ServiceReviewUpdateComponent } from '../update/service-review-update.component';
import { ServiceReviewRoutingResolveService } from './service-review-routing-resolve.service';

const serviceReviewRoute: Routes = [
  {
    path: '',
    component: ServiceReviewComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceReviewDetailComponent,
    resolve: {
      serviceReview: ServiceReviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceReviewUpdateComponent,
    resolve: {
      serviceReview: ServiceReviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceReviewUpdateComponent,
    resolve: {
      serviceReview: ServiceReviewRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceReviewRoute)],
  exports: [RouterModule],
})
export class ServiceReviewRoutingModule {}
