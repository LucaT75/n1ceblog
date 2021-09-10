import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExpert, Expert } from '../expert.model';

import { ExpertService } from './expert.service';

describe('Service Tests', () => {
  describe('Expert Service', () => {
    let service: ExpertService;
    let httpMock: HttpTestingController;
    let elemDefault: IExpert;
    let expectedResult: IExpert | IExpert[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExpertService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        shortBio: 'AAAAAAA',
        expertise: 'AAAAAAA',
        rating: 0,
        reviews: 0,
        candidatureVotes: 0,
        candidatureEndTime: 'AAAAAAA',
        candidatureStakedAmount: 0,
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

      it('should create a Expert', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Expert()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Expert', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            shortBio: 'BBBBBB',
            expertise: 'BBBBBB',
            rating: 1,
            reviews: 1,
            candidatureVotes: 1,
            candidatureEndTime: 'BBBBBB',
            candidatureStakedAmount: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Expert', () => {
        const patchObject = Object.assign(
          {
            expertise: 'BBBBBB',
            rating: 1,
          },
          new Expert()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Expert', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            shortBio: 'BBBBBB',
            expertise: 'BBBBBB',
            rating: 1,
            reviews: 1,
            candidatureVotes: 1,
            candidatureEndTime: 'BBBBBB',
            candidatureStakedAmount: 1,
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

      it('should delete a Expert', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExpertToCollectionIfMissing', () => {
        it('should add a Expert to an empty array', () => {
          const expert: IExpert = { id: 'ABC' };
          expectedResult = service.addExpertToCollectionIfMissing([], expert);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(expert);
        });

        it('should not add a Expert to an array that contains it', () => {
          const expert: IExpert = { id: 'ABC' };
          const expertCollection: IExpert[] = [
            {
              ...expert,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addExpertToCollectionIfMissing(expertCollection, expert);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Expert to an array that doesn't contain it", () => {
          const expert: IExpert = { id: 'ABC' };
          const expertCollection: IExpert[] = [{ id: 'CBA' }];
          expectedResult = service.addExpertToCollectionIfMissing(expertCollection, expert);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(expert);
        });

        it('should add only unique Expert to an array', () => {
          const expertArray: IExpert[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Island Loan Assistant' }];
          const expertCollection: IExpert[] = [{ id: 'ABC' }];
          expectedResult = service.addExpertToCollectionIfMissing(expertCollection, ...expertArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const expert: IExpert = { id: 'ABC' };
          const expert2: IExpert = { id: 'CBA' };
          expectedResult = service.addExpertToCollectionIfMissing([], expert, expert2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(expert);
          expect(expectedResult).toContain(expert2);
        });

        it('should accept null and undefined values', () => {
          const expert: IExpert = { id: 'ABC' };
          expectedResult = service.addExpertToCollectionIfMissing([], null, expert, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(expert);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
