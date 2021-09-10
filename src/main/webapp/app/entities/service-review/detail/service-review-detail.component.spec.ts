import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ServiceReviewDetailComponent } from './service-review-detail.component';

describe('Component Tests', () => {
  describe('ServiceReview Management Detail Component', () => {
    let comp: ServiceReviewDetailComponent;
    let fixture: ComponentFixture<ServiceReviewDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ServiceReviewDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ serviceReview: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(ServiceReviewDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ServiceReviewDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load serviceReview on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.serviceReview).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
