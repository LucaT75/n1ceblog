import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBlogPost, getBlogPostIdentifier } from '../blog-post.model';

export type EntityResponseType = HttpResponse<IBlogPost>;
export type EntityArrayResponseType = HttpResponse<IBlogPost[]>;

@Injectable({ providedIn: 'root' })
export class BlogPostService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/blog-posts');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(blogPost: IBlogPost): Observable<EntityResponseType> {
    return this.http.post<IBlogPost>(this.resourceUrl, blogPost, { observe: 'response' });
  }

  update(blogPost: IBlogPost): Observable<EntityResponseType> {
    return this.http.put<IBlogPost>(`${this.resourceUrl}/${getBlogPostIdentifier(blogPost) as string}`, blogPost, { observe: 'response' });
  }

  partialUpdate(blogPost: IBlogPost): Observable<EntityResponseType> {
    return this.http.patch<IBlogPost>(`${this.resourceUrl}/${getBlogPostIdentifier(blogPost) as string}`, blogPost, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IBlogPost>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBlogPost[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBlogPostToCollectionIfMissing(blogPostCollection: IBlogPost[], ...blogPostsToCheck: (IBlogPost | null | undefined)[]): IBlogPost[] {
    const blogPosts: IBlogPost[] = blogPostsToCheck.filter(isPresent);
    if (blogPosts.length > 0) {
      const blogPostCollectionIdentifiers = blogPostCollection.map(blogPostItem => getBlogPostIdentifier(blogPostItem)!);
      const blogPostsToAdd = blogPosts.filter(blogPostItem => {
        const blogPostIdentifier = getBlogPostIdentifier(blogPostItem);
        if (blogPostIdentifier == null || blogPostCollectionIdentifiers.includes(blogPostIdentifier)) {
          return false;
        }
        blogPostCollectionIdentifiers.push(blogPostIdentifier);
        return true;
      });
      return [...blogPostsToAdd, ...blogPostCollection];
    }
    return blogPostCollection;
  }
}
