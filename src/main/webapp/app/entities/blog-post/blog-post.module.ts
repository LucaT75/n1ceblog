import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BlogPostComponent } from './list/blog-post.component';
import { BlogPostDetailComponent } from './detail/blog-post-detail.component';
import { BlogPostUpdateComponent } from './update/blog-post-update.component';
import { BlogPostDeleteDialogComponent } from './delete/blog-post-delete-dialog.component';
import { BlogPostRoutingModule } from './route/blog-post-routing.module';

@NgModule({
  imports: [SharedModule, BlogPostRoutingModule],
  declarations: [BlogPostComponent, BlogPostDetailComponent, BlogPostUpdateComponent, BlogPostDeleteDialogComponent],
  entryComponents: [BlogPostDeleteDialogComponent],
})
export class BlogPostModule {}
