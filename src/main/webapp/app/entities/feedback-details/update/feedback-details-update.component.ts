import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFeedbackSubType } from 'app/entities/feedback-sub-type/feedback-sub-type.model';
import { FeedbackSubTypeService } from 'app/entities/feedback-sub-type/service/feedback-sub-type.service';
import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';
import { FeedbackResponderService } from 'app/entities/feedback-responder/service/feedback-responder.service';
import { FeedbackDetailsService } from '../service/feedback-details.service';
import { IFeedbackDetails } from '../feedback-details.model';
import { FeedbackDetailsFormService, FeedbackDetailsFormGroup } from './feedback-details-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feedback-details-update',
  templateUrl: './feedback-details-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedbackDetailsUpdateComponent implements OnInit {
  isSaving = false;
  feedbackDetails: IFeedbackDetails | null = null;

  feedbackSubTypesSharedCollection: IFeedbackSubType[] = [];
  feedbackRespondersSharedCollection: IFeedbackResponder[] = [];

  protected feedbackDetailsService = inject(FeedbackDetailsService);
  protected feedbackDetailsFormService = inject(FeedbackDetailsFormService);
  protected feedbackSubTypeService = inject(FeedbackSubTypeService);
  protected feedbackResponderService = inject(FeedbackResponderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedbackDetailsFormGroup = this.feedbackDetailsFormService.createFeedbackDetailsFormGroup();

  compareFeedbackSubType = (o1: IFeedbackSubType | null, o2: IFeedbackSubType | null): boolean =>
    this.feedbackSubTypeService.compareFeedbackSubType(o1, o2);

  compareFeedbackResponder = (o1: IFeedbackResponder | null, o2: IFeedbackResponder | null): boolean =>
    this.feedbackResponderService.compareFeedbackResponder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackDetails }) => {
      this.feedbackDetails = feedbackDetails;
      if (feedbackDetails) {
        this.updateForm(feedbackDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackDetails = this.feedbackDetailsFormService.getFeedbackDetails(this.editForm);
    if (feedbackDetails.id !== null) {
      this.subscribeToSaveResponse(this.feedbackDetailsService.update(feedbackDetails));
    } else {
      this.subscribeToSaveResponse(this.feedbackDetailsService.create(feedbackDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackDetails>>): void {
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

  protected updateForm(feedbackDetails: IFeedbackDetails): void {
    this.feedbackDetails = feedbackDetails;
    this.feedbackDetailsFormService.resetForm(this.editForm, feedbackDetails);

    this.feedbackSubTypesSharedCollection = this.feedbackSubTypeService.addFeedbackSubTypeToCollectionIfMissing<IFeedbackSubType>(
      this.feedbackSubTypesSharedCollection,
      feedbackDetails.feedbackSubType,
    );
    this.feedbackRespondersSharedCollection = this.feedbackResponderService.addFeedbackResponderToCollectionIfMissing<IFeedbackResponder>(
      this.feedbackRespondersSharedCollection,
      feedbackDetails.responder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.feedbackSubTypeService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackSubType[]>) => res.body ?? []))
      .pipe(
        map((feedbackSubTypes: IFeedbackSubType[]) =>
          this.feedbackSubTypeService.addFeedbackSubTypeToCollectionIfMissing<IFeedbackSubType>(
            feedbackSubTypes,
            this.feedbackDetails?.feedbackSubType,
          ),
        ),
      )
      .subscribe((feedbackSubTypes: IFeedbackSubType[]) => (this.feedbackSubTypesSharedCollection = feedbackSubTypes));

    this.feedbackResponderService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackResponder[]>) => res.body ?? []))
      .pipe(
        map((feedbackResponders: IFeedbackResponder[]) =>
          this.feedbackResponderService.addFeedbackResponderToCollectionIfMissing<IFeedbackResponder>(
            feedbackResponders,
            this.feedbackDetails?.responder,
          ),
        ),
      )
      .subscribe((feedbackResponders: IFeedbackResponder[]) => (this.feedbackRespondersSharedCollection = feedbackResponders));
  }
}
