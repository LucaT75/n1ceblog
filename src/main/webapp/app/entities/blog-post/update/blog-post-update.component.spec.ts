jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BlogPostService } from '../service/blog-post.service';
import { IBlogPost, BlogPost } from '../blog-post.model';

import { BlogPostUpdateComponent } from './blog-post-update.component';

describe('Component Tests', () => {
  describe('BlogPost Management Update Component', () => {
    let comp: BlogPostUpdateComponent;
    let fixture: ComponentFixture<BlogPostUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let blogPostService: BlogPostService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BlogPostUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BlogPostUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BlogPostUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      blogPostService = TestBed.inject(BlogPostService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const blogPost: IBlogPost = { id: 'CBA' };

        activatedRoute.data = of({ blogPost });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(blogPost));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blogPost = { id: 'ABC' };
        spyOn(blogPostService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blogPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blogPost }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(blogPostService.update).toHaveBeenCalledWith(blogPost);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blogPost = new BlogPost();
        spyOn(blogPostService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blogPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blogPost }));
        saveSubject.complete();

        // THEN
        expect(blogPostService.create).toHaveBeenCalledWith(blogPost);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blogPost = { id: 'ABC' };
        spyOn(blogPostService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blogPost });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(blogPostService.update).toHaveBeenCalledWith(blogPost);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
