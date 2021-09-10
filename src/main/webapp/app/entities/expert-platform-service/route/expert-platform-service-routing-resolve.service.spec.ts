jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExpertPlatformService, ExpertPlatformService } from '../expert-platform-service.model';
import { ExpertPlatformServiceService } from '../service/expert-platform-service.service';

import { ExpertPlatformServiceRoutingResolveService } from './expert-platform-service-routing-resolve.service';

describe('Service Tests', () => {
  describe('ExpertPlatformService routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ExpertPlatformServiceRoutingResolveService;
    let service: ExpertPlatformServiceService;
    let resultExpertPlatformService: IExpertPlatformService | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ExpertPlatformServiceRoutingResolveService);
      service = TestBed.inject(ExpertPlatformServiceService);
      resultExpertPlatformService = undefined;
    });

    describe('resolve', () => {
      it('should return IExpertPlatformService returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpertPlatformService = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultExpertPlatformService).toEqual({ id: 'ABC' });
      });

      it('should return new IExpertPlatformService if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpertPlatformService = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultExpertPlatformService).toEqual(new ExpertPlatformService());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultExpertPlatformService = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultExpertPlatformService).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
