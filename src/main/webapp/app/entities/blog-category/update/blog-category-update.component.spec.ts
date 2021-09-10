jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BlogCategoryService } from '../service/blog-category.service';
import { IBlogCategory, BlogCategory } from '../blog-category.model';

import { BlogCategoryUpdateComponent } from './blog-category-update.component';

describe('Component Tests', () => {
  describe('BlogCategory Management Update Component', () => {
    let comp: BlogCategoryUpdateComponent;
    let fixture: ComponentFixture<BlogCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let blogCategoryService: BlogCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BlogCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BlogCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BlogCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      blogCategoryService = TestBed.inject(BlogCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const blogCategory: IBlogCategory = { id: 'CBA' };

        activatedRoute.data = of({ blogCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(blogCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blogCategory = { id: 'ABC' };
        spyOn(blogCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blogCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blogCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(blogCategoryService.update).toHaveBeenCalledWith(blogCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blogCategory = new BlogCategory();
        spyOn(blogCategoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blogCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: blogCategory }));
        saveSubject.complete();

        // THEN
        expect(blogCategoryService.create).toHaveBeenCalledWith(blogCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const blogCategory = { id: 'ABC' };
        spyOn(blogCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ blogCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(blogCategoryService.update).toHaveBeenCalledWith(blogCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
