import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IServiceCategory, ServiceCategory } from '../service-category.model';
import { ServiceCategoryService } from '../service/service-category.service';

@Component({
  selector: 'jhi-service-category-update',
  templateUrl: './service-category-update.component.html',
})
export class ServiceCategoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    icon: [],
    services: [],
  });

  constructor(
    protected serviceCategoryService: ServiceCategoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceCategory }) => {
      this.updateForm(serviceCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceCategory = this.createFromForm();
    if (serviceCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.serviceCategoryService.update(serviceCategory));
    } else {
      this.subscribeToSaveResponse(this.serviceCategoryService.create(serviceCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceCategory>>): void {
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

  protected updateForm(serviceCategory: IServiceCategory): void {
    this.editForm.patchValue({
      id: serviceCategory.id,
      title: serviceCategory.title,
      icon: serviceCategory.icon,
      services: serviceCategory.services,
    });
  }

  protected createFromForm(): IServiceCategory {
    return {
      ...new ServiceCategory(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      icon: this.editForm.get(['icon'])!.value,
      services: this.editForm.get(['services'])!.value,
    };
  }
}
