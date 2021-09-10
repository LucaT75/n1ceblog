import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ServiceReviewComponent } from './list/service-review.component';
import { ServiceReviewDetailComponent } from './detail/service-review-detail.component';
import { ServiceReviewUpdateComponent } from './update/service-review-update.component';
import { ServiceReviewDeleteDialogComponent } from './delete/service-review-delete-dialog.component';
import { ServiceReviewRoutingModule } from './route/service-review-routing.module';

@NgModule({
  imports: [SharedModule, ServiceReviewRoutingModule],
  declarations: [ServiceReviewComponent, ServiceReviewDetailComponent, ServiceReviewUpdateComponent, ServiceReviewDeleteDialogComponent],
  entryComponents: [ServiceReviewDeleteDialogComponent],
})
export class ServiceReviewModule {}
