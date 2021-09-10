import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceReview } from '../service-review.model';
import { ServiceReviewService } from '../service/service-review.service';

@Component({
  templateUrl: './service-review-delete-dialog.component.html',
})
export class ServiceReviewDeleteDialogComponent {
  serviceReview?: IServiceReview;

  constructor(protected serviceReviewService: ServiceReviewService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.serviceReviewService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
