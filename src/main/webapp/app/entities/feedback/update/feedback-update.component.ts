import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IRatingScale } from 'app/entities/rating-scale/rating-scale.model';
import { RatingScaleService } from 'app/entities/rating-scale/service/rating-scale.service';
import { FeedbackStatus } from 'app/entities/enumerations/feedback-status.model';
import { FeedbackService } from '../service/feedback.service';
import { IFeedback } from '../feedback.model';
import { FeedbackFormService, FeedbackFormGroup } from './feedback-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feedback-update',
  templateUrl: './feedback-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedbackUpdateComponent implements OnInit {
  isSaving = false;
  feedback: IFeedback | null = null;
  feedbackStatusValues = Object.keys(FeedbackStatus);

  employeesSharedCollection: IEmployee[] = [];
  ratingScalesSharedCollection: IRatingScale[] = [];

  protected feedbackService = inject(FeedbackService);
  protected feedbackFormService = inject(FeedbackFormService);
  protected employeeService = inject(EmployeeService);
  protected ratingScaleService = inject(RatingScaleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedbackFormGroup = this.feedbackFormService.createFeedbackFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareRatingScale = (o1: IRatingScale | null, o2: IRatingScale | null): boolean => this.ratingScaleService.compareRatingScale(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedback }) => {
      this.feedback = feedback;
      if (feedback) {
        this.updateForm(feedback);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedback = this.feedbackFormService.getFeedback(this.editForm);
    if (feedback.id !== null) {
      this.subscribeToSaveResponse(this.feedbackService.update(feedback));
    } else {
      this.subscribeToSaveResponse(this.feedbackService.create(feedback));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedback>>): void {
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

  protected updateForm(feedback: IFeedback): void {
    this.feedback = feedback;
    this.feedbackFormService.resetForm(this.editForm, feedback);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      feedback.requester,
    );
    this.ratingScalesSharedCollection = this.ratingScaleService.addRatingScaleToCollectionIfMissing<IRatingScale>(
      this.ratingScalesSharedCollection,
      feedback.ratingScale,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.feedback?.requester),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.ratingScaleService
      .query()
      .pipe(map((res: HttpResponse<IRatingScale[]>) => res.body ?? []))
      .pipe(
        map((ratingScales: IRatingScale[]) =>
          this.ratingScaleService.addRatingScaleToCollectionIfMissing<IRatingScale>(ratingScales, this.feedback?.ratingScale),
        ),
      )
      .subscribe((ratingScales: IRatingScale[]) => (this.ratingScalesSharedCollection = ratingScales));
  }
}
