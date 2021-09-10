import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServiceCategoryComponent } from '../list/service-category.component';
import { ServiceCategoryDetailComponent } from '../detail/service-category-detail.component';
import { ServiceCategoryUpdateComponent } from '../update/service-category-update.component';
import { ServiceCategoryRoutingResolveService } from './service-category-routing-resolve.service';

const serviceCategoryRoute: Routes = [
  {
    path: '',
    component: ServiceCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceCategoryDetailComponent,
    resolve: {
      serviceCategory: ServiceCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceCategoryUpdateComponent,
    resolve: {
      serviceCategory: ServiceCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceCategoryUpdateComponent,
    resolve: {
      serviceCategory: ServiceCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceCategoryRoute)],
  exports: [RouterModule],
})
export class ServiceCategoryRoutingModule {}
