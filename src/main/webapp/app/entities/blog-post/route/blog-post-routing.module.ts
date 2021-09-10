import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BlogPostComponent } from '../list/blog-post.component';
import { BlogPostDetailComponent } from '../detail/blog-post-detail.component';
import { BlogPostUpdateComponent } from '../update/blog-post-update.component';
import { BlogPostRoutingResolveService } from './blog-post-routing-resolve.service';

const blogPostRoute: Routes = [
  {
    path: '',
    component: BlogPostComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlogPostDetailComponent,
    resolve: {
      blogPost: BlogPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlogPostUpdateComponent,
    resolve: {
      blogPost: BlogPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlogPostUpdateComponent,
    resolve: {
      blogPost: BlogPostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(blogPostRoute)],
  exports: [RouterModule],
})
export class BlogPostRoutingModule {}
