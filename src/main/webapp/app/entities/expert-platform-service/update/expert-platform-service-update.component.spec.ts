jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExpertPlatformServiceService } from '../service/expert-platform-service.service';
import { IExpertPlatformService, ExpertPlatformService } from '../expert-platform-service.model';

import { ExpertPlatformServiceUpdateComponent } from './expert-platform-service-update.component';

describe('Component Tests', () => {
  describe('ExpertPlatformService Management Update Component', () => {
    let comp: ExpertPlatformServiceUpdateComponent;
    let fixture: ComponentFixture<ExpertPlatformServiceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let expertPlatformServiceService: ExpertPlatformServiceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpertPlatformServiceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExpertPlatformServiceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpertPlatformServiceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      expertPlatformServiceService = TestBed.inject(ExpertPlatformServiceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const expertPlatformService: IExpertPlatformService = { id: 'CBA' };

        activatedRoute.data = of({ expertPlatformService });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(expertPlatformService));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const expertPlatformService = { id: 'ABC' };
        spyOn(expertPlatformServiceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ expertPlatformService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expertPlatformService }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(expertPlatformServiceService.update).toHaveBeenCalledWith(expertPlatformService);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const expertPlatformService = new ExpertPlatformService();
        spyOn(expertPlatformServiceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ expertPlatformService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expertPlatformService }));
        saveSubject.complete();

        // THEN
        expect(expertPlatformServiceService.create).toHaveBeenCalledWith(expertPlatformService);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const expertPlatformService = { id: 'ABC' };
        spyOn(expertPlatformServiceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ expertPlatformService });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(expertPlatformServiceService.update).toHaveBeenCalledWith(expertPlatformService);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
