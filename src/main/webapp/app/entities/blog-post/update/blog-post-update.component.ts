import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBlogPost, BlogPost } from '../blog-post.model';
import { BlogPostService } from '../service/blog-post.service';

@Component({
  selector: 'jhi-blog-post-update',
  templateUrl: './blog-post-update.component.html',
})
export class BlogPostUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    content: [],
    snippet: [],
    expertId: [],
    featuredImg: [],
    category: [],
    publishingTime: [],
  });

  constructor(protected blogPostService: BlogPostService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blogPost }) => {
      this.updateForm(blogPost);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const blogPost = this.createFromForm();
    if (blogPost.id !== undefined) {
      this.subscribeToSaveResponse(this.blogPostService.update(blogPost));
    } else {
      this.subscribeToSaveResponse(this.blogPostService.create(blogPost));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlogPost>>): void {
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

  protected updateForm(blogPost: IBlogPost): void {
    this.editForm.patchValue({
      id: blogPost.id,
      title: blogPost.title,
      content: blogPost.content,
      snippet: blogPost.snippet,
      expertId: blogPost.expertId,
      featuredImg: blogPost.featuredImg,
      category: blogPost.category,
      publishingTime: blogPost.publishingTime,
    });
  }

  protected createFromForm(): IBlogPost {
    return {
      ...new BlogPost(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      snippet: this.editForm.get(['snippet'])!.value,
      expertId: this.editForm.get(['expertId'])!.value,
      featuredImg: this.editForm.get(['featuredImg'])!.value,
      category: this.editForm.get(['category'])!.value,
      publishingTime: this.editForm.get(['publishingTime'])!.value,
    };
  }
}
