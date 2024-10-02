import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFeedback } from 'app/entities/feedback/feedback.model';
import { FeedbackService } from 'app/entities/feedback/service/feedback.service';
import { IExtraquestion } from '../extraquestion.model';
import { ExtraquestionService } from '../service/extraquestion.service';
import { ExtraquestionFormService, ExtraquestionFormGroup } from './extraquestion-form.service';

@Component({
  standalone: true,
  selector: 'jhi-extraquestion-update',
  templateUrl: './extraquestion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExtraquestionUpdateComponent implements OnInit {
  isSaving = false;
  extraquestion: IExtraquestion | null = null;

  feedbacksSharedCollection: IFeedback[] = [];

  protected extraquestionService = inject(ExtraquestionService);
  protected extraquestionFormService = inject(ExtraquestionFormService);
  protected feedbackService = inject(FeedbackService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExtraquestionFormGroup = this.extraquestionFormService.createExtraquestionFormGroup();

  compareFeedback = (o1: IFeedback | null, o2: IFeedback | null): boolean => this.feedbackService.compareFeedback(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extraquestion }) => {
      this.extraquestion = extraquestion;
      if (extraquestion) {
        this.updateForm(extraquestion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const extraquestion = this.extraquestionFormService.getExtraquestion(this.editForm);
    if (extraquestion.id !== null) {
      this.subscribeToSaveResponse(this.extraquestionService.update(extraquestion));
    } else {
      this.subscribeToSaveResponse(this.extraquestionService.create(extraquestion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtraquestion>>): void {
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

  protected updateForm(extraquestion: IExtraquestion): void {
    this.extraquestion = extraquestion;
    this.extraquestionFormService.resetForm(this.editForm, extraquestion);

    this.feedbacksSharedCollection = this.feedbackService.addFeedbackToCollectionIfMissing<IFeedback>(
      this.feedbacksSharedCollection,
      extraquestion.feedback,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.feedbackService
      .query()
      .pipe(map((res: HttpResponse<IFeedback[]>) => res.body ?? []))
      .pipe(
        map((feedbacks: IFeedback[]) =>
          this.feedbackService.addFeedbackToCollectionIfMissing<IFeedback>(feedbacks, this.extraquestion?.feedback),
        ),
      )
      .subscribe((feedbacks: IFeedback[]) => (this.feedbacksSharedCollection = feedbacks));
  }
}
