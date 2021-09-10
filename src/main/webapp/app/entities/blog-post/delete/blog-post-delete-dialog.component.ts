import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBlogPost } from '../blog-post.model';
import { BlogPostService } from '../service/blog-post.service';

@Component({
  templateUrl: './blog-post-delete-dialog.component.html',
})
export class BlogPostDeleteDialogComponent {
  blogPost?: IBlogPost;

  constructor(protected blogPostService: BlogPostService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.blogPostService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
