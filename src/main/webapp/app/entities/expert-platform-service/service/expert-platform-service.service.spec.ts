import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExpertPlatformService, ExpertPlatformService } from '../expert-platform-service.model';

import { ExpertPlatformServiceService } from './expert-platform-service.service';

describe('Service Tests', () => {
  describe('ExpertPlatformService Service', () => {
    let service: ExpertPlatformServiceService;
    let httpMock: HttpTestingController;
    let elemDefault: IExpertPlatformService;
    let expectedResult: IExpertPlatformService | IExpertPlatformService[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExpertPlatformServiceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        title: 'AAAAAAA',
        content: 'AAAAAAA',
        expertId: 'AAAAAAA',
        featuredImg: 'AAAAAAA',
        category: 'AAAAAAA',
        startingPrice: 0,
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

      it('should create a ExpertPlatformService', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ExpertPlatformService()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ExpertPlatformService', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            content: 'BBBBBB',
            expertId: 'BBBBBB',
            featuredImg: 'BBBBBB',
            category: 'BBBBBB',
            startingPrice: 1,
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

      it('should partial update a ExpertPlatformService', () => {
        const patchObject = Object.assign(
          {
            startingPrice: 1,
          },
          new ExpertPlatformService()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ExpertPlatformService', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            content: 'BBBBBB',
            expertId: 'BBBBBB',
            featuredImg: 'BBBBBB',
            category: 'BBBBBB',
            startingPrice: 1,
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

      it('should delete a ExpertPlatformService', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExpertPlatformServiceToCollectionIfMissing', () => {
        it('should add a ExpertPlatformService to an empty array', () => {
          const expertPlatformService: IExpertPlatformService = { id: 'ABC' };
          expectedResult = service.addExpertPlatformServiceToCollectionIfMissing([], expertPlatformService);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(expertPlatformService);
        });

        it('should not add a ExpertPlatformService to an array that contains it', () => {
          const expertPlatformService: IExpertPlatformService = { id: 'ABC' };
          const expertPlatformServiceCollection: IExpertPlatformService[] = [
            {
              ...expertPlatformService,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addExpertPlatformServiceToCollectionIfMissing(expertPlatformServiceCollection, expertPlatformService);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ExpertPlatformService to an array that doesn't contain it", () => {
          const expertPlatformService: IExpertPlatformService = { id: 'ABC' };
          const expertPlatformServiceCollection: IExpertPlatformService[] = [{ id: 'CBA' }];
          expectedResult = service.addExpertPlatformServiceToCollectionIfMissing(expertPlatformServiceCollection, expertPlatformService);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(expertPlatformService);
        });

        it('should add only unique ExpertPlatformService to an array', () => {
          const expertPlatformServiceArray: IExpertPlatformService[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Guam Salad neural' }];
          const expertPlatformServiceCollection: IExpertPlatformService[] = [{ id: 'ABC' }];
          expectedResult = service.addExpertPlatformServiceToCollectionIfMissing(
            expertPlatformServiceCollection,
            ...expertPlatformServiceArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const expertPlatformService: IExpertPlatformService = { id: 'ABC' };
          const expertPlatformService2: IExpertPlatformService = { id: 'CBA' };
          expectedResult = service.addExpertPlatformServiceToCollectionIfMissing([], expertPlatformService, expertPlatformService2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(expertPlatformService);
          expect(expectedResult).toContain(expertPlatformService2);
        });

        it('should accept null and undefined values', () => {
          const expertPlatformService: IExpertPlatformService = { id: 'ABC' };
          expectedResult = service.addExpertPlatformServiceToCollectionIfMissing([], null, expertPlatformService, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(expertPlatformService);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
