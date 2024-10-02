import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRatingScale } from '../rating-scale.model';
import { RatingScaleService } from '../service/rating-scale.service';
import { RatingScaleFormService, RatingScaleFormGroup } from './rating-scale-form.service';

@Component({
  standalone: true,
  selector: 'jhi-rating-scale-update',
  templateUrl: './rating-scale-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RatingScaleUpdateComponent implements OnInit {
  isSaving = false;
  ratingScale: IRatingScale | null = null;

  protected ratingScaleService = inject(RatingScaleService);
  protected ratingScaleFormService = inject(RatingScaleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RatingScaleFormGroup = this.ratingScaleFormService.createRatingScaleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ratingScale }) => {
      this.ratingScale = ratingScale;
      if (ratingScale) {
        this.updateForm(ratingScale);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ratingScale = this.ratingScaleFormService.getRatingScale(this.editForm);
    if (ratingScale.id !== null) {
      this.subscribeToSaveResponse(this.ratingScaleService.update(ratingScale));
    } else {
      this.subscribeToSaveResponse(this.ratingScaleService.create(ratingScale));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRatingScale>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(ratingScale: IRatingScale): void {
    this.ratingScale = ratingScale;
    this.ratingScaleFormService.resetForm(this.editForm, ratingScale);
  }
}
