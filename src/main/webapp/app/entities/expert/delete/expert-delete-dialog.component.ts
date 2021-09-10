import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpert } from '../expert.model';
import { ExpertService } from '../service/expert.service';

@Component({
  templateUrl: './expert-delete-dialog.component.html',
})
export class ExpertDeleteDialogComponent {
  expert?: IExpert;

  constructor(protected expertService: ExpertService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.expertService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
