import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ExpertPlatformServiceComponent } from './list/expert-platform-service.component';
import { ExpertPlatformServiceDetailComponent } from './detail/expert-platform-service-detail.component';
import { ExpertPlatformServiceUpdateComponent } from './update/expert-platform-service-update.component';
import { ExpertPlatformServiceDeleteDialogComponent } from './delete/expert-platform-service-delete-dialog.component';
import { ExpertPlatformServiceRoutingModule } from './route/expert-platform-service-routing.module';

@NgModule({
  imports: [SharedModule, ExpertPlatformServiceRoutingModule],
  declarations: [
    ExpertPlatformServiceComponent,
    ExpertPlatformServiceDetailComponent,
    ExpertPlatformServiceUpdateComponent,
    ExpertPlatformServiceDeleteDialogComponent,
  ],
  entryComponents: [ExpertPlatformServiceDeleteDialogComponent],
})
export class ExpertPlatformServiceModule {}
