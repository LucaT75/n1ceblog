import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBlogCategory, BlogCategory } from '../blog-category.model';
import { BlogCategoryService } from '../service/blog-category.service';

@Component({
  selector: 'jhi-blog-category-update',
  templateUrl: './blog-category-update.component.html',
})
export class BlogCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    articles: [],
    artcilesPerRow: [],
  });

  constructor(protected blogCategoryService: BlogCategoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blogCategory }) => {
      this.updateForm(blogCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const blogCategory = this.createFromForm();
    if (blogCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.blogCategoryService.update(blogCategory));
    } else {
      this.subscribeToSaveResponse(this.blogCategoryService.create(blogCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlogCategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(blogCategory: IBlogCategory): void {
    this.editForm.patchValue({
      id: blogCategory.id,
      title: blogCategory.title,
      articles: blogCategory.articles,
      artcilesPerRow: blogCategory.artcilesPerRow,
    });
  }

  protected createFromForm(): IBlogCategory {
    return {
      ...new BlogCategory(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      articles: this.editForm.get(['articles'])!.value,
      artcilesPerRow: this.editForm.get(['artcilesPerRow'])!.value,
    };
  }
}
