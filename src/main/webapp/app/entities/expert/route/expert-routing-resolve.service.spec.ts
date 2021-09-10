jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExpert, Expert } from '../expert.model';
import { ExpertService } from '../service/expert.service';

import { ExpertRoutingResolveService } from './expert-routing-resolve.service';

describe('Service Tests', () => {
  describe('Expert routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExpertRoutingResolveService;
    let service: ExpertService;
    let resultExpert: IExpert | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExpertRoutingResolveService);
      service = TestBed.inject(ExpertService);
      resultExpert = undefined;
    });

    describe('resolve', () => {
      it('should return IExpert returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpert = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultExpert).toEqual({ id: 'ABC' });
      });

      it('should return new IExpert if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpert = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExpert).toEqual(new Expert());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpert = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultExpert).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
