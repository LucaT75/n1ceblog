import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { BlogCategoryComponent } from './list/blog-category.component';
import { BlogCategoryDetailComponent } from './detail/blog-category-detail.component';
import { BlogCategoryUpdateComponent } from './update/blog-category-update.component';
import { BlogCategoryDeleteDialogComponent } from './delete/blog-category-delete-dialog.component';
import { BlogCategoryRoutingModule } from './route/blog-category-routing.module';

@NgModule({
  imports: [SharedModule, BlogCategoryRoutingModule],
  declarations: [BlogCategoryComponent, BlogCategoryDetailComponent, BlogCategoryUpdateComponent, BlogCategoryDeleteDialogComponent],
  entryComponents: [BlogCategoryDeleteDialogComponent],
})
export class BlogCategoryModule {}
