import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IExpert, Expert } from '../expert.model';
import { ExpertService } from '../service/expert.service';

@Component({
  selector: 'jhi-expert-update',
  templateUrl: './expert-update.component.html',
})
export class ExpertUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    shortBio: [],
    expertise: [],
    rating: [],
    reviews: [],
    candidatureVotes: [],
    candidatureEndTime: [],
    candidatureStakedAmount: [],
  });

  constructor(protected expertService: ExpertService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expert }) => {
      this.updateForm(expert);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expert = this.createFromForm();
    if (expert.id !== undefined) {
      this.subscribeToSaveResponse(this.expertService.update(expert));
    } else {
      this.subscribeToSaveResponse(this.expertService.create(expert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpert>>): void {
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

  protected updateForm(expert: IExpert): void {
    this.editForm.patchValue({
      id: expert.id,
      shortBio: expert.shortBio,
      expertise: expert.expertise,
      rating: expert.rating,
      reviews: expert.reviews,
      candidatureVotes: expert.candidatureVotes,
      candidatureEndTime: expert.candidatureEndTime,
      candidatureStakedAmount: expert.candidatureStakedAmount,
    });
  }

  protected createFromForm(): IExpert {
    return {
      ...new Expert(),
      id: this.editForm.get(['id'])!.value,
      shortBio: this.editForm.get(['shortBio'])!.value,
      expertise: this.editForm.get(['expertise'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      reviews: this.editForm.get(['reviews'])!.value,
      candidatureVotes: this.editForm.get(['candidatureVotes'])!.value,
      candidatureEndTime: this.editForm.get(['candidatureEndTime'])!.value,
      candidatureStakedAmount: this.editForm.get(['candidatureStakedAmount'])!.value,
    };
  }
}
