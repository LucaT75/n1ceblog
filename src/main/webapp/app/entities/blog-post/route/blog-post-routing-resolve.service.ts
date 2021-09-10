import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBlogPost, BlogPost } from '../blog-post.model';
import { BlogPostService } from '../service/blog-post.service';

@Injectable({ providedIn: 'root' })
export class BlogPostRoutingResolveService implements Resolve<IBlogPost> {
  constructor(protected service: BlogPostService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBlogPost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((blogPost: HttpResponse<BlogPost>) => {
          if (blogPost.body) {
            return of(blogPost.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BlogPost());
  }
}
