import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ServiceCategoryComponent } from './list/service-category.component';
import { ServiceCategoryDetailComponent } from './detail/service-category-detail.component';
import { ServiceCategoryUpdateComponent } from './update/service-category-update.component';
import { ServiceCategoryDeleteDialogComponent } from './delete/service-category-delete-dialog.component';
import { ServiceCategoryRoutingModule } from './route/service-category-routing.module';

@NgModule({
  imports: [SharedModule, ServiceCategoryRoutingModule],
  declarations: [
    ServiceCategoryComponent,
    ServiceCategoryDetailComponent,
    ServiceCategoryUpdateComponent,
    ServiceCategoryDeleteDialogComponent,
  ],
  entryComponents: [ServiceCategoryDeleteDialogComponent],
})
export class ServiceCategoryModule {}
