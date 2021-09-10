jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IServiceReview, ServiceReview } from '../service-review.model';
import { ServiceReviewService } from '../service/service-review.service';

import { ServiceReviewRoutingResolveService } from './service-review-routing-resolve.service';

describe('Service Tests', () => {
  describe('ServiceReview routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ServiceReviewRoutingResolveService;
    let service: ServiceReviewService;
    let resultServiceReview: IServiceReview | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ServiceReviewRoutingResolveService);
      service = TestBed.inject(ServiceReviewService);
      resultServiceReview = undefined;
    });

    describe('resolve', () => {
      it('should return IServiceReview returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceReview = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultServiceReview).toEqual({ id: 'ABC' });
      });

      it('should return new IServiceReview if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceReview = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultServiceReview).toEqual(new ServiceReview());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultServiceReview = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultServiceReview).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
