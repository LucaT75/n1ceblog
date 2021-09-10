jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ExpertPlatformServiceService } from '../service/expert-platform-service.service';

import { ExpertPlatformServiceDeleteDialogComponent } from './expert-platform-service-delete-dialog.component';

describe('Component Tests', () => {
  describe('ExpertPlatformService Management Delete Component', () => {
    let comp: ExpertPlatformServiceDeleteDialogComponent;
    let fixture: ComponentFixture<ExpertPlatformServiceDeleteDialogComponent>;
    let service: ExpertPlatformServiceService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpertPlatformServiceDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ExpertPlatformServiceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpertPlatformServiceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExpertPlatformServiceService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete('ABC');
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith('ABC');
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
