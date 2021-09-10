import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServiceReview, ServiceReview } from '../service-review.model';

import { ServiceReviewService } from './service-review.service';

describe('Service Tests', () => {
  describe('ServiceReview Service', () => {
    let service: ServiceReviewService;
    let httpMock: HttpTestingController;
    let elemDefault: IServiceReview;
    let expectedResult: IServiceReview | IServiceReview[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ServiceReviewService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        serviceId: 'AAAAAAA',
        userId: 'AAAAAAA',
        rating: 0,
        comment: 'AAAAAAA',
        publishingTime: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ServiceReview', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ServiceReview()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ServiceReview', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            serviceId: 'BBBBBB',
            userId: 'BBBBBB',
            rating: 1,
            comment: 'BBBBBB',
            publishingTime: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ServiceReview', () => {
        const patchObject = Object.assign(
          {
            serviceId: 'BBBBBB',
            rating: 1,
            comment: 'BBBBBB',
            publishingTime: 'BBBBBB',
          },
          new ServiceReview()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ServiceReview', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            serviceId: 'BBBBBB',
            userId: 'BBBBBB',
            rating: 1,
            comment: 'BBBBBB',
            publishingTime: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ServiceReview', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addServiceReviewToCollectionIfMissing', () => {
        it('should add a ServiceReview to an empty array', () => {
          const serviceReview: IServiceReview = { id: 'ABC' };
          expectedResult = service.addServiceReviewToCollectionIfMissing([], serviceReview);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(serviceReview);
        });

        it('should not add a ServiceReview to an array that contains it', () => {
          const serviceReview: IServiceReview = { id: 'ABC' };
          const serviceReviewCollection: IServiceReview[] = [
            {
              ...serviceReview,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addServiceReviewToCollectionIfMissing(serviceReviewCollection, serviceReview);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ServiceReview to an array that doesn't contain it", () => {
          const serviceReview: IServiceReview = { id: 'ABC' };
          const serviceReviewCollection: IServiceReview[] = [{ id: 'CBA' }];
          expectedResult = service.addServiceReviewToCollectionIfMissing(serviceReviewCollection, serviceReview);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(serviceReview);
        });

        it('should add only unique ServiceReview to an array', () => {
          const serviceReviewArray: IServiceReview[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Directives Rupee override' }];
          const serviceReviewCollection: IServiceReview[] = [{ id: 'ABC' }];
          expectedResult = service.addServiceReviewToCollectionIfMissing(serviceReviewCollection, ...serviceReviewArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const serviceReview: IServiceReview = { id: 'ABC' };
          const serviceReview2: IServiceReview = { id: 'CBA' };
          expectedResult = service.addServiceReviewToCollectionIfMissing([], serviceReview, serviceReview2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(serviceReview);
          expect(expectedResult).toContain(serviceReview2);
        });

        it('should accept null and undefined values', () => {
          const serviceReview: IServiceReview = { id: 'ABC' };
          expectedResult = service.addServiceReviewToCollectionIfMissing([], null, serviceReview, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(serviceReview);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
