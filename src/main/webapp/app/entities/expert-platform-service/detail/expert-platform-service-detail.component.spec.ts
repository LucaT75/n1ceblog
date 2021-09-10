import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpertPlatformServiceDetailComponent } from './expert-platform-service-detail.component';

describe('Component Tests', () => {
  describe('ExpertPlatformService Management Detail Component', () => {
    let comp: ExpertPlatformServiceDetailComponent;
    let fixture: ComponentFixture<ExpertPlatformServiceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExpertPlatformServiceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ expertPlatformService: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(ExpertPlatformServiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpertPlatformServiceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load expertPlatformService on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.expertPlatformService).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
