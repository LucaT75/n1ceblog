import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServiceCategoryDetailComponent } from './service-category-detail.component';

describe('Component Tests', () => {
  describe('ServiceCategory Management Detail Component', () => {
    let comp: ServiceCategoryDetailComponent;
    let fixture: ComponentFixture<ServiceCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ServiceCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ serviceCategory: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(ServiceCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServiceCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load serviceCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.serviceCategory).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
