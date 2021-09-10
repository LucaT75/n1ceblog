import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBlogCategory } from '../blog-category.model';

@Component({
  selector: 'jhi-blog-category-detail',
  templateUrl: './blog-category-detail.component.html',
})
export class BlogCategoryDetailComponent implements OnInit {
  blogCategory: IBlogCategory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blogCategory }) => {
      this.blogCategory = blogCategory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
