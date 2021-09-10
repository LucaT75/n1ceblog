import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BlogCategoryComponent } from '../list/blog-category.component';
import { BlogCategoryDetailComponent } from '../detail/blog-category-detail.component';
import { BlogCategoryUpdateComponent } from '../update/blog-category-update.component';
import { BlogCategoryRoutingResolveService } from './blog-category-routing-resolve.service';

const blogCategoryRoute: Routes = [
  {
    path: '',
    component: BlogCategoryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlogCategoryDetailComponent,
    resolve: {
      blogCategory: BlogCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlogCategoryUpdateComponent,
    resolve: {
      blogCategory: BlogCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlogCategoryUpdateComponent,
    resolve: {
      blogCategory: BlogCategoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(blogCategoryRoute)],
  exports: [RouterModule],
})
export class BlogCategoryRoutingModule {}
