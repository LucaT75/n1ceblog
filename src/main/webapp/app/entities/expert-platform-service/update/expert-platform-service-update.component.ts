import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IExpertPlatformService, ExpertPlatformService } from '../expert-platform-service.model';
import { ExpertPlatformServiceService } from '../service/expert-platform-service.service';

@Component({
  selector: 'jhi-expert-platform-service-update',
  templateUrl: './expert-platform-service-update.component.html',
})
export class ExpertPlatformServiceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    content: [],
    expertId: [],
    featuredImg: [],
    category: [],
    startingPrice: [],
    publishingTime: [],
  });

  constructor(
    protected expertPlatformServiceService: ExpertPlatformServiceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expertPlatformService }) => {
      this.updateForm(expertPlatformService);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expertPlatformService = this.createFromForm();
    if (expertPlatformService.id !== undefined) {
      this.subscribeToSaveResponse(this.expertPlatformServiceService.update(expertPlatformService));
    } else {
      this.subscribeToSaveResponse(this.expertPlatformServiceService.create(expertPlatformService));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpertPlatformService>>): void {
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

  protected updateForm(expertPlatformService: IExpertPlatformService): void {
    this.editForm.patchValue({
      id: expertPlatformService.id,
      title: expertPlatformService.title,
      content: expertPlatformService.content,
      expertId: expertPlatformService.expertId,
      featuredImg: expertPlatformService.featuredImg,
      category: expertPlatformService.category,
      startingPrice: expertPlatformService.startingPrice,
      publishingTime: expertPlatformService.publishingTime,
    });
  }

  protected createFromForm(): IExpertPlatformService {
    return {
      ...new ExpertPlatformService(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      content: this.editForm.get(['content'])!.value,
      expertId: this.editForm.get(['expertId'])!.value,
      featuredImg: this.editForm.get(['featuredImg'])!.value,
      category: this.editForm.get(['category'])!.value,
      startingPrice: this.editForm.get(['startingPrice'])!.value,
      publishingTime: this.editForm.get(['publishingTime'])!.value,
    };
  }
}
