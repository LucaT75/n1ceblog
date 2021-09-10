jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ServiceReviewService } from '../service/service-review.service';
import { IServiceReview, ServiceReview } from '../service-review.model';

import { ServiceReviewUpdateComponent } from './service-review-update.component';

describe('Component Tests', () => {
  describe('ServiceReview Management Update Component', () => {
    let comp: ServiceReviewUpdateComponent;
    let fixture: ComponentFixture<ServiceReviewUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let serviceReviewService: ServiceReviewService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ServiceReviewUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ServiceReviewUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ServiceReviewUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      serviceReviewService = TestBed.inject(ServiceReviewService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const serviceReview: IServiceReview = { id: 'CBA' };

        activatedRoute.data = of({ serviceReview });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(serviceReview));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const serviceReview = { id: 'ABC' };
        spyOn(serviceReviewService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceReview });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: serviceReview }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(serviceReviewService.update).toHaveBeenCalledWith(serviceReview);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const serviceReview = new ServiceReview();
        spyOn(serviceReviewService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceReview });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: serviceReview }));
        saveSubject.complete();

        // THEN
        expect(serviceReviewService.create).toHaveBeenCalledWith(serviceReview);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const serviceReview = { id: 'ABC' };
        spyOn(serviceReviewService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ serviceReview });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(serviceReviewService.update).toHaveBeenCalledWith(serviceReview);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
