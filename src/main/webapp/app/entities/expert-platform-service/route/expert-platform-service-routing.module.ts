import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpertPlatformServiceComponent } from '../list/expert-platform-service.component';
import { ExpertPlatformServiceDetailComponent } from '../detail/expert-platform-service-detail.component';
import { ExpertPlatformServiceUpdateComponent } from '../update/expert-platform-service-update.component';
import { ExpertPlatformServiceRoutingResolveService } from './expert-platform-service-routing-resolve.service';

const expertPlatformServiceRoute: Routes = [
  {
    path: '',
    component: ExpertPlatformServiceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpertPlatformServiceDetailComponent,
    resolve: {
      expertPlatformService: ExpertPlatformServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpertPlatformServiceUpdateComponent,
    resolve: {
      expertPlatformService: ExpertPlatformServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpertPlatformServiceUpdateComponent,
    resolve: {
      expertPlatformService: ExpertPlatformServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expertPlatformServiceRoute)],
  exports: [RouterModule],
})
export class ExpertPlatformServiceRoutingModule {}
