import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceCategory } from '../service-category.model';
import { ServiceCategoryService } from '../service/service-category.service';

@Component({
  templateUrl: './service-category-delete-dialog.component.html',
})
export class ServiceCategoryDeleteDialogComponent {
  serviceCategory?: IServiceCategory;

  constructor(protected serviceCategoryService: ServiceCategoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.serviceCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
