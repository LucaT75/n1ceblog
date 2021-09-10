import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBlogCategory, BlogCategory } from '../blog-category.model';

import { BlogCategoryService } from './blog-category.service';

describe('Service Tests', () => {
  describe('BlogCategory Service', () => {
    let service: BlogCategoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IBlogCategory;
    let expectedResult: IBlogCategory | IBlogCategory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BlogCategoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        title: 'AAAAAAA',
        articles: 'AAAAAAA',
        artcilesPerRow: 0,
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

      it('should create a BlogCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BlogCategory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BlogCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            articles: 'BBBBBB',
            artcilesPerRow: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BlogCategory', () => {
        const patchObject = Object.assign(
          {
            artcilesPerRow: 1,
          },
          new BlogCategory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BlogCategory', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            articles: 'BBBBBB',
            artcilesPerRow: 1,
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

      it('should delete a BlogCategory', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBlogCategoryToCollectionIfMissing', () => {
        it('should add a BlogCategory to an empty array', () => {
          const blogCategory: IBlogCategory = { id: 'ABC' };
          expectedResult = service.addBlogCategoryToCollectionIfMissing([], blogCategory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(blogCategory);
        });

        it('should not add a BlogCategory to an array that contains it', () => {
          const blogCategory: IBlogCategory = { id: 'ABC' };
          const blogCategoryCollection: IBlogCategory[] = [
            {
              ...blogCategory,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addBlogCategoryToCollectionIfMissing(blogCategoryCollection, blogCategory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BlogCategory to an array that doesn't contain it", () => {
          const blogCategory: IBlogCategory = { id: 'ABC' };
          const blogCategoryCollection: IBlogCategory[] = [{ id: 'CBA' }];
          expectedResult = service.addBlogCategoryToCollectionIfMissing(blogCategoryCollection, blogCategory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(blogCategory);
        });

        it('should add only unique BlogCategory to an array', () => {
          const blogCategoryArray: IBlogCategory[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'collaborative Court Metal' }];
          const blogCategoryCollection: IBlogCategory[] = [{ id: 'ABC' }];
          expectedResult = service.addBlogCategoryToCollectionIfMissing(blogCategoryCollection, ...blogCategoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const blogCategory: IBlogCategory = { id: 'ABC' };
          const blogCategory2: IBlogCategory = { id: 'CBA' };
          expectedResult = service.addBlogCategoryToCollectionIfMissing([], blogCategory, blogCategory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(blogCategory);
          expect(expectedResult).toContain(blogCategory2);
        });

        it('should accept null and undefined values', () => {
          const blogCategory: IBlogCategory = { id: 'ABC' };
          expectedResult = service.addBlogCategoryToCollectionIfMissing([], null, blogCategory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(blogCategory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
