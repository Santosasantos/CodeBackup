import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';
import { FeedbackResponderService } from 'app/entities/feedback-responder/service/feedback-responder.service';
import { RecommendationValue } from 'app/entities/enumerations/recommendation-value.model';
import { TeachOtherService } from '../service/teach-other.service';
import { ITeachOther } from '../teach-other.model';
import { TeachOtherFormService, TeachOtherFormGroup } from './teach-other-form.service';

@Component({
  standalone: true,
  selector: 'jhi-teach-other-update',
  templateUrl: './teach-other-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TeachOtherUpdateComponent implements OnInit {
  isSaving = false;
  teachOther: ITeachOther | null = null;
  recommendationValueValues = Object.keys(RecommendationValue);

  feedbackRespondersSharedCollection: IFeedbackResponder[] = [];

  protected teachOtherService = inject(TeachOtherService);
  protected teachOtherFormService = inject(TeachOtherFormService);
  protected feedbackResponderService = inject(FeedbackResponderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TeachOtherFormGroup = this.teachOtherFormService.createTeachOtherFormGroup();

  compareFeedbackResponder = (o1: IFeedbackResponder | null, o2: IFeedbackResponder | null): boolean =>
    this.feedbackResponderService.compareFeedbackResponder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teachOther }) => {
      this.teachOther = teachOther;
      if (teachOther) {
        this.updateForm(teachOther);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teachOther = this.teachOtherFormService.getTeachOther(this.editForm);
    if (teachOther.id !== null) {
      this.subscribeToSaveResponse(this.teachOtherService.update(teachOther));
    } else {
      this.subscribeToSaveResponse(this.teachOtherService.create(teachOther));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeachOther>>): void {
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

  protected updateForm(teachOther: ITeachOther): void {
    this.teachOther = teachOther;
    this.teachOtherFormService.resetForm(this.editForm, teachOther);

    this.feedbackRespondersSharedCollection = this.feedbackResponderService.addFeedbackResponderToCollectionIfMissing<IFeedbackResponder>(
      this.feedbackRespondersSharedCollection,
      teachOther.responder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.feedbackResponderService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackResponder[]>) => res.body ?? []))
      .pipe(
        map((feedbackResponders: IFeedbackResponder[]) =>
          this.feedbackResponderService.addFeedbackResponderToCollectionIfMissing<IFeedbackResponder>(
            feedbackResponders,
            this.teachOther?.responder,
          ),
        ),
      )
      .subscribe((feedbackResponders: IFeedbackResponder[]) => (this.feedbackRespondersSharedCollection = feedbackResponders));
  }
}
