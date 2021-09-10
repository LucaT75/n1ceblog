import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBlogPost, BlogPost } from '../blog-post.model';

import { BlogPostService } from './blog-post.service';

describe('Service Tests', () => {
  describe('BlogPost Service', () => {
    let service: BlogPostService;
    let httpMock: HttpTestingController;
    let elemDefault: IBlogPost;
    let expectedResult: IBlogPost | IBlogPost[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BlogPostService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 'AAAAAAA',
        title: 'AAAAAAA',
        content: 'AAAAAAA',
        snippet: 'AAAAAAA',
        expertId: 'AAAAAAA',
        featuredImg: 'AAAAAAA',
        category: 'AAAAAAA',
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

      it('should create a BlogPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BlogPost()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BlogPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            content: 'BBBBBB',
            snippet: 'BBBBBB',
            expertId: 'BBBBBB',
            featuredImg: 'BBBBBB',
            category: 'BBBBBB',
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

      it('should partial update a BlogPost', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            content: 'BBBBBB',
            expertId: 'BBBBBB',
            category: 'BBBBBB',
          },
          new BlogPost()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BlogPost', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            title: 'BBBBBB',
            content: 'BBBBBB',
            snippet: 'BBBBBB',
            expertId: 'BBBBBB',
            featuredImg: 'BBBBBB',
            category: 'BBBBBB',
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

      it('should delete a BlogPost', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBlogPostToCollectionIfMissing', () => {
        it('should add a BlogPost to an empty array', () => {
          const blogPost: IBlogPost = { id: 'ABC' };
          expectedResult = service.addBlogPostToCollectionIfMissing([], blogPost);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(blogPost);
        });

        it('should not add a BlogPost to an array that contains it', () => {
          const blogPost: IBlogPost = { id: 'ABC' };
          const blogPostCollection: IBlogPost[] = [
            {
              ...blogPost,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addBlogPostToCollectionIfMissing(blogPostCollection, blogPost);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BlogPost to an array that doesn't contain it", () => {
          const blogPost: IBlogPost = { id: 'ABC' };
          const blogPostCollection: IBlogPost[] = [{ id: 'CBA' }];
          expectedResult = service.addBlogPostToCollectionIfMissing(blogPostCollection, blogPost);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(blogPost);
        });

        it('should add only unique BlogPost to an array', () => {
          const blogPostArray: IBlogPost[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'invoice' }];
          const blogPostCollection: IBlogPost[] = [{ id: 'ABC' }];
          expectedResult = service.addBlogPostToCollectionIfMissing(blogPostCollection, ...blogPostArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const blogPost: IBlogPost = { id: 'ABC' };
          const blogPost2: IBlogPost = { id: 'CBA' };
          expectedResult = service.addBlogPostToCollectionIfMissing([], blogPost, blogPost2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(blogPost);
          expect(expectedResult).toContain(blogPost2);
        });

        it('should accept null and undefined values', () => {
          const blogPost: IBlogPost = { id: 'ABC' };
          expectedResult = service.addBlogPostToCollectionIfMissing([], null, blogPost, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(blogPost);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
