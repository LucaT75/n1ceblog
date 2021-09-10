import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBlogCategory, BlogCategory } from '../blog-category.model';
import { BlogCategoryService } from '../service/blog-category.service';

@Injectable({ providedIn: 'root' })
export class BlogCategoryRoutingResolveService implements Resolve<IBlogCategory> {
  constructor(protected service: BlogCategoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBlogCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((blogCategory: HttpResponse<BlogCategory>) => {
          if (blogCategory.body) {
            return of(blogCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BlogCategory());
  }
}
