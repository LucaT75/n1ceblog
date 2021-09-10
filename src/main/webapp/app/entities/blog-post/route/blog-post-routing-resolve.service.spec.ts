jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBlogPost, BlogPost } from '../blog-post.model';
import { BlogPostService } from '../service/blog-post.service';

import { BlogPostRoutingResolveService } from './blog-post-routing-resolve.service';

describe('Service Tests', () => {
  describe('BlogPost routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BlogPostRoutingResolveService;
    let service: BlogPostService;
    let resultBlogPost: IBlogPost | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BlogPostRoutingResolveService);
      service = TestBed.inject(BlogPostService);
      resultBlogPost = undefined;
    });

    describe('resolve', () => {
      it('should return IBlogPost returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlogPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultBlogPost).toEqual({ id: 'ABC' });
      });

      it('should return new IBlogPost if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlogPost = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBlogPost).toEqual(new BlogPost());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlogPost = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultBlogPost).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
