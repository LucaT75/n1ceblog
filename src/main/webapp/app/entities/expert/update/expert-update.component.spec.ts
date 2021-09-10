jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExpertService } from '../service/expert.service';
import { IExpert, Expert } from '../expert.model';

import { ExpertUpdateComponent } from './expert-update.component';

describe('Component Tests', () => {
  describe('Expert Management Update Component', () => {
    let comp: ExpertUpdateComponent;
    let fixture: ComponentFixture<ExpertUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let expertService: ExpertService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpertUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExpertUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpertUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      expertService = TestBed.inject(ExpertService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const expert: IExpert = { id: 'CBA' };

        activatedRoute.data = of({ expert });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(expert));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const expert = { id: 'ABC' };
        spyOn(expertService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ expert });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expert }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(expertService.update).toHaveBeenCalledWith(expert);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const expert = new Expert();
        spyOn(expertService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ expert });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expert }));
        saveSubject.complete();

        // THEN
        expect(expertService.create).toHaveBeenCalledWith(expert);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const expert = { id: 'ABC' };
        spyOn(expertService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ expert });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(expertService.update).toHaveBeenCalledWith(expert);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
