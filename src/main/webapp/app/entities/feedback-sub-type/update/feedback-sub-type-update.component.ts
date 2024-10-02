import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFeedbackType } from 'app/entities/feedback-type/feedback-type.model';
import { FeedbackTypeService } from 'app/entities/feedback-type/service/feedback-type.service';
import { IFeedbackSubType } from '../feedback-sub-type.model';
import { FeedbackSubTypeService } from '../service/feedback-sub-type.service';
import { FeedbackSubTypeFormService, FeedbackSubTypeFormGroup } from './feedback-sub-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feedback-sub-type-update',
  templateUrl: './feedback-sub-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedbackSubTypeUpdateComponent implements OnInit {
  isSaving = false;
  feedbackSubType: IFeedbackSubType | null = null;

  feedbackTypesSharedCollection: IFeedbackType[] = [];

  protected feedbackSubTypeService = inject(FeedbackSubTypeService);
  protected feedbackSubTypeFormService = inject(FeedbackSubTypeFormService);
  protected feedbackTypeService = inject(FeedbackTypeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedbackSubTypeFormGroup = this.feedbackSubTypeFormService.createFeedbackSubTypeFormGroup();

  compareFeedbackType = (o1: IFeedbackType | null, o2: IFeedbackType | null): boolean =>
    this.feedbackTypeService.compareFeedbackType(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackSubType }) => {
      this.feedbackSubType = feedbackSubType;
      if (feedbackSubType) {
        this.updateForm(feedbackSubType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackSubType = this.feedbackSubTypeFormService.getFeedbackSubType(this.editForm);
    if (feedbackSubType.id !== null) {
      this.subscribeToSaveResponse(this.feedbackSubTypeService.update(feedbackSubType));
    } else {
      this.subscribeToSaveResponse(this.feedbackSubTypeService.create(feedbackSubType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackSubType>>): void {
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

  protected updateForm(feedbackSubType: IFeedbackSubType): void {
    this.feedbackSubType = feedbackSubType;
    this.feedbackSubTypeFormService.resetForm(this.editForm, feedbackSubType);

    this.feedbackTypesSharedCollection = this.feedbackTypeService.addFeedbackTypeToCollectionIfMissing<IFeedbackType>(
      this.feedbackTypesSharedCollection,
      feedbackSubType.feedbackType,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.feedbackTypeService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackType[]>) => res.body ?? []))
      .pipe(
        map((feedbackTypes: IFeedbackType[]) =>
          this.feedbackTypeService.addFeedbackTypeToCollectionIfMissing<IFeedbackType>(feedbackTypes, this.feedbackSubType?.feedbackType),
        ),
      )
      .subscribe((feedbackTypes: IFeedbackType[]) => (this.feedbackTypesSharedCollection = feedbackTypes));
  }
}
