import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBlogCategory, getBlogCategoryIdentifier } from '../blog-category.model';

export type EntityResponseType = HttpResponse<IBlogCategory>;
export type EntityArrayResponseType = HttpResponse<IBlogCategory[]>;

@Injectable({ providedIn: 'root' })
export class BlogCategoryService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/blog-categories');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(blogCategory: IBlogCategory): Observable<EntityResponseType> {
    return this.http.post<IBlogCategory>(this.resourceUrl, blogCategory, { observe: 'response' });
  }

  update(blogCategory: IBlogCategory): Observable<EntityResponseType> {
    return this.http.put<IBlogCategory>(`${this.resourceUrl}/${getBlogCategoryIdentifier(blogCategory) as string}`, blogCategory, {
      observe: 'response',
    });
  }

  partialUpdate(blogCategory: IBlogCategory): Observable<EntityResponseType> {
    return this.http.patch<IBlogCategory>(`${this.resourceUrl}/${getBlogCategoryIdentifier(blogCategory) as string}`, blogCategory, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBlogCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBlogCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBlogCategoryToCollectionIfMissing(
    blogCategoryCollection: IBlogCategory[],
    ...blogCategoriesToCheck: (IBlogCategory | null | undefined)[]
  ): IBlogCategory[] {
    const blogCategories: IBlogCategory[] = blogCategoriesToCheck.filter(isPresent);
    if (blogCategories.length > 0) {
      const blogCategoryCollectionIdentifiers = blogCategoryCollection.map(
        blogCategoryItem => getBlogCategoryIdentifier(blogCategoryItem)!
      );
      const blogCategoriesToAdd = blogCategories.filter(blogCategoryItem => {
        const blogCategoryIdentifier = getBlogCategoryIdentifier(blogCategoryItem);
        if (blogCategoryIdentifier == null || blogCategoryCollectionIdentifiers.includes(blogCategoryIdentifier)) {
          return false;
        }
        blogCategoryCollectionIdentifiers.push(blogCategoryIdentifier);
        return true;
      });
      return [...blogCategoriesToAdd, ...blogCategoryCollection];
    }
    return blogCategoryCollection;
  }
}
