import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpertPlatformService } from '../expert-platform-service.model';
import { ExpertPlatformServiceService } from '../service/expert-platform-service.service';

@Component({
  templateUrl: './expert-platform-service-delete-dialog.component.html',
})
export class ExpertPlatformServiceDeleteDialogComponent {
  expertPlatformService?: IExpertPlatformService;

  constructor(protected expertPlatformServiceService: ExpertPlatformServiceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.expertPlatformServiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
