jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IServiceCategory, ServiceCategory } from '../service-category.model';
import { ServiceCategoryService } from '../service/service-category.service';

import { ServiceCategoryRoutingResolveService } from './service-category-routing-resolve.service';

describe('Service Tests', () => {
  describe('ServiceCategory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ServiceCategoryRoutingResolveService;
    let service: ServiceCategoryService;
    let resultServiceCategory: IServiceCategory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ServiceCategoryRoutingResolveService);
      service = TestBed.inject(ServiceCategoryService);
      resultServiceCategory = undefined;
    });

    describe('resolve', () => {
      it('should return IServiceCategory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultServiceCategory).toEqual({ id: 'ABC' });
      });

      it('should return new IServiceCategory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceCategory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultServiceCategory).toEqual(new ServiceCategory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceCategory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultServiceCategory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
