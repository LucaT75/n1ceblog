import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBlogPost } from '../blog-post.model';

@Component({
  selector: 'jhi-blog-post-detail',
  templateUrl: './blog-post-detail.component.html',
})
export class BlogPostDetailComponent implements OnInit {
  blogPost: IBlogPost | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blogPost }) => {
      this.blogPost = blogPost;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
