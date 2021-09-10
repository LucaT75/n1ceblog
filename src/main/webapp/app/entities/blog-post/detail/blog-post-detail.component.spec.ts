import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BlogPostDetailComponent } from './blog-post-detail.component';

describe('Component Tests', () => {
  describe('BlogPost Management Detail Component', () => {
    let comp: BlogPostDetailComponent;
    let fixture: ComponentFixture<BlogPostDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BlogPostDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ blogPost: { id: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(BlogPostDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BlogPostDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load blogPost on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.blogPost).toEqual(jasmine.objectContaining({ id: 'ABC' }));
      });
    });
  });
});
