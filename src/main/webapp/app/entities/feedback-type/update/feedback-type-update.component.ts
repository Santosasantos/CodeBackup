import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFeedbackType } from '../feedback-type.model';
import { FeedbackTypeService } from '../service/feedback-type.service';
import { FeedbackTypeFormService, FeedbackTypeFormGroup } from './feedback-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feedback-type-update',
  templateUrl: './feedback-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedbackTypeUpdateComponent implements OnInit {
  isSaving = false;
  feedbackType: IFeedbackType | null = null;

  protected feedbackTypeService = inject(FeedbackTypeService);
  protected feedbackTypeFormService = inject(FeedbackTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedbackTypeFormGroup = this.feedbackTypeFormService.createFeedbackTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackType }) => {
      this.feedbackType = feedbackType;
      if (feedbackType) {
        this.updateForm(feedbackType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackType = this.feedbackTypeFormService.getFeedbackType(this.editForm);
    if (feedbackType.id !== null) {
      this.subscribeToSaveResponse(this.feedbackTypeService.update(feedbackType));
    } else {
      this.subscribeToSaveResponse(this.feedbackTypeService.create(feedbackType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackType>>): void {
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

  protected updateForm(feedbackType: IFeedbackType): void {
    this.feedbackType = feedbackType;
    this.feedbackTypeFormService.resetForm(this.editForm, feedbackType);
  }
}
