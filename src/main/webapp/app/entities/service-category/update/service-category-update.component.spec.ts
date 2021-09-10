jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ServiceCategoryService } from '../service/service-category.service';
import { IServiceCategory, ServiceCategory } from '../service-category.model';

import { ServiceCategoryUpdateComponent } from './service-category-update.component';

describe('Component Tests', () => {
  describe('ServiceCategory Management Update Component', () => {
    let comp: ServiceCategoryUpdateComponent;
    let fixture: ComponentFixture<ServiceCategoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let serviceCategoryService: ServiceCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ServiceCategoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ServiceCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceCategoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      serviceCategoryService = TestBed.inject(ServiceCategoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const serviceCategory: IServiceCategory = { id: 'CBA' };

        activatedRoute.data = of({ serviceCategory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(serviceCategory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const serviceCategory = { id: 'ABC' };
        spyOn(serviceCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: serviceCategory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(serviceCategoryService.update).toHaveBeenCalledWith(serviceCategory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const serviceCategory = new ServiceCategory();
        spyOn(serviceCategoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: serviceCategory }));
        saveSubject.complete();

        // THEN
        expect(serviceCategoryService.create).toHaveBeenCalledWith(serviceCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const serviceCategory = { id: 'ABC' };
        spyOn(serviceCategoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceCategory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(serviceCategoryService.update).toHaveBeenCalledWith(serviceCategory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
