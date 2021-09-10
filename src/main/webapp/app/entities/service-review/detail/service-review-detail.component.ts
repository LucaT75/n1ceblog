import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServiceReview } from '../service-review.model';

@Component({
  selector: 'jhi-service-review-detail',
  templateUrl: './service-review-detail.component.html',
})
export class ServiceReviewDetailComponent implements OnInit {
  serviceReview: IServiceReview | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceReview }) => {
      this.serviceReview = serviceReview;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
