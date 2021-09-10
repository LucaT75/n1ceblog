import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IServiceReview, ServiceReview } from '../service-review.model';
import { ServiceReviewService } from '../service/service-review.service';

@Component({
  selector: 'jhi-service-review-update',
  templateUrl: './service-review-update.component.html',
})
export class ServiceReviewUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    serviceId: [],
    userId: [],
    rating: [],
    comment: [],
    publishingTime: [],
  });

  constructor(protected serviceReviewService: ServiceReviewService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceReview }) => {
      this.updateForm(serviceReview);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceReview = this.createFromForm();
    if (serviceReview.id !== undefined) {
      this.subscribeToSaveResponse(this.serviceReviewService.update(serviceReview));
    } else {
      this.subscribeToSaveResponse(this.serviceReviewService.create(serviceReview));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceReview>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(serviceReview: IServiceReview): void {
    this.editForm.patchValue({
      id: serviceReview.id,
      serviceId: serviceReview.serviceId,
      userId: serviceReview.userId,
      rating: serviceReview.rating,
      comment: serviceReview.comment,
      publishingTime: serviceReview.publishingTime,
    });
  }

  protected createFromForm(): IServiceReview {
    return {
      ...new ServiceReview(),
      id: this.editForm.get(['id'])!.value,
      serviceId: this.editForm.get(['serviceId'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      publishingTime: this.editForm.get(['publishingTime'])!.value,
    };
  }
}
