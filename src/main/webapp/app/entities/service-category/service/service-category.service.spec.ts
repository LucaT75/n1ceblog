import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServiceCategory, ServiceCategory } from '../service-category.model';

import { ServiceCategoryService } from './service-category.service';

describe('Service Tests', () => {
  describe('ServiceCategory Service', () => {
    let service: ServiceCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IServiceCategory;
    let expectedResult: IServiceCategory | IServiceCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ServiceCategoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        title: 'AAAAAAA',
        icon: 'AAAAAAA',
        services: 'AAAAAAA',
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

      it('should create a ServiceCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ServiceCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ServiceCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            icon: 'BBBBBB',
            services: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ServiceCategory', () => {
        const patchObject = Object.assign(
          {
            services: 'BBBBBB',
          },
          new ServiceCategory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ServiceCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            icon: 'BBBBBB',
            services: 'BBBBBB',
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

      it('should delete a ServiceCategory', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addServiceCategoryToCollectionIfMissing', () => {
        it('should add a ServiceCategory to an empty array', () => {
          const serviceCategory: IServiceCategory = { id: 'ABC' };
          expectedResult = service.addServiceCategoryToCollectionIfMissing([], serviceCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(serviceCategory);
        });

        it('should not add a ServiceCategory to an array that contains it', () => {
          const serviceCategory: IServiceCategory = { id: 'ABC' };
          const serviceCategoryCollection: IServiceCategory[] = [
            {
              ...serviceCategory,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addServiceCategoryToCollectionIfMissing(serviceCategoryCollection, serviceCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ServiceCategory to an array that doesn't contain it", () => {
          const serviceCategory: IServiceCategory = { id: 'ABC' };
          const serviceCategoryCollection: IServiceCategory[] = [{ id: 'CBA' }];
          expectedResult = service.addServiceCategoryToCollectionIfMissing(serviceCategoryCollection, serviceCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(serviceCategory);
        });

        it('should add only unique ServiceCategory to an array', () => {
          const serviceCategoryArray: IServiceCategory[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Belarus' }];
          const serviceCategoryCollection: IServiceCategory[] = [{ id: 'ABC' }];
          expectedResult = service.addServiceCategoryToCollectionIfMissing(serviceCategoryCollection, ...serviceCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const serviceCategory: IServiceCategory = { id: 'ABC' };
          const serviceCategory2: IServiceCategory = { id: 'CBA' };
          expectedResult = service.addServiceCategoryToCollectionIfMissing([], serviceCategory, serviceCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(serviceCategory);
          expect(expectedResult).toContain(serviceCategory2);
        });

        it('should accept null and undefined values', () => {
          const serviceCategory: IServiceCategory = { id: 'ABC' };
          expectedResult = service.addServiceCategoryToCollectionIfMissing([], null, serviceCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(serviceCategory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
