import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlogCategoryDetailComponent } from './blog-category-detail.component';

describe('Component Tests', () => {
  describe('BlogCategory Management Detail Component', () => {
    let comp: BlogCategoryDetailComponent;
    let fixture: ComponentFixture<BlogCategoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BlogCategoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ blogCategory: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(BlogCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BlogCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load blogCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.blogCategory).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
