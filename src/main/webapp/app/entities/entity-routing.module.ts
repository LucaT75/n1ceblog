import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'blog-post',
        data: { pageTitle: 'n1CeblogApp.blogPost.home.title' },
        loadChildren: () => import('./blog-post/blog-post.module').then(m => m.BlogPostModule),
      },
      {
        path: 'blog-category',
        data: { pageTitle: 'n1CeblogApp.blogCategory.home.title' },
        loadChildren: () => import('./blog-category/blog-category.module').then(m => m.BlogCategoryModule),
      },
      {
        path: 'expert',
        data: { pageTitle: 'n1CeblogApp.expert.home.title' },
        loadChildren: () => import('./expert/expert.module').then(m => m.ExpertModule),
      },
      {
        path: 'service-category',
        data: { pageTitle: 'n1CeblogApp.serviceCategory.home.title' },
        loadChildren: () => import('./service-category/service-category.module').then(m => m.ServiceCategoryModule),
      },
      {
        path: 'expert-platform-service',
        data: { pageTitle: 'n1CeblogApp.expertPlatformService.home.title' },
        loadChildren: () => import('./expert-platform-service/expert-platform-service.module').then(m => m.ExpertPlatformServiceModule),
      },
      {
        path: 'service-review',
        data: { pageTitle: 'n1CeblogApp.serviceReview.home.title' },
        loadChildren: () => import('./service-review/service-review.module').then(m => m.ServiceReviewModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
