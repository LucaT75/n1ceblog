jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBlogCategory, BlogCategory } from '../blog-category.model';
import { BlogCategoryService } from '../service/blog-category.service';

import { BlogCategoryRoutingResolveService } from './blog-category-routing-resolve.service';

describe('Service Tests', () => {
  describe('BlogCategory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BlogCategoryRoutingResolveService;
    let service: BlogCategoryService;
    let resultBlogCategory: IBlogCategory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BlogCategoryRoutingResolveService);
      service = TestBed.inject(BlogCategoryService);
      resultBlogCategory = undefined;
    });

    describe('resolve', () => {
      it('should return IBlogCategory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlogCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultBlogCategory).toEqual({ id: 'ABC' });
      });

      it('should return new IBlogCategory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlogCategory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBlogCategory).toEqual(new BlogCategory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBlogCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultBlogCategory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
