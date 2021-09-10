import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlogCategory } from '../blog-category.model';
import { BlogCategoryService } from '../service/blog-category.service';

@Component({
  templateUrl: './blog-category-delete-dialog.component.html',
})
export class BlogCategoryDeleteDialogComponent {
  blogCategory?: IBlogCategory;

  constructor(protected blogCategoryService: BlogCategoryService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.blogCategoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
