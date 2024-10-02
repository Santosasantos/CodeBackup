import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IFeedback } from 'app/entities/feedback/feedback.model';
import { FeedbackService } from 'app/entities/feedback/service/feedback.service';
import { ResponderCategory } from 'app/entities/enumerations/responder-category.model';
import { FeedbackStatus } from 'app/entities/enumerations/feedback-status.model';
import { FeedbackResponderService } from '../service/feedback-responder.service';
import { IFeedbackResponder } from '../feedback-responder.model';
import { FeedbackResponderFormService, FeedbackResponderFormGroup } from './feedback-responder-form.service';

@Component({
  standalone: true,
  selector: 'jhi-feedback-responder-update',
  templateUrl: './feedback-responder-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeedbackResponderUpdateComponent implements OnInit {
  isSaving = false;
  feedbackResponder: IFeedbackResponder | null = null;
  responderCategoryValues = Object.keys(ResponderCategory);
  feedbackStatusValues = Object.keys(FeedbackStatus);

  employeesSharedCollection: IEmployee[] = [];
  feedbacksSharedCollection: IFeedback[] = [];

  protected feedbackResponderService = inject(FeedbackResponderService);
  protected feedbackResponderFormService = inject(FeedbackResponderFormService);
  protected employeeService = inject(EmployeeService);
  protected feedbackService = inject(FeedbackService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeedbackResponderFormGroup = this.feedbackResponderFormService.createFeedbackResponderFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareFeedback = (o1: IFeedback | null, o2: IFeedback | null): boolean => this.feedbackService.compareFeedback(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedbackResponder }) => {
      this.feedbackResponder = feedbackResponder;
      if (feedbackResponder) {
        this.updateForm(feedbackResponder);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedbackResponder = this.feedbackResponderFormService.getFeedbackResponder(this.editForm);
    if (feedbackResponder.id !== null) {
      this.subscribeToSaveResponse(this.feedbackResponderService.update(feedbackResponder));
    } else {
      this.subscribeToSaveResponse(this.feedbackResponderService.create(feedbackResponder));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedbackResponder>>): void {
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

  protected updateForm(feedbackResponder: IFeedbackResponder): void {
    this.feedbackResponder = feedbackResponder;
    this.feedbackResponderFormService.resetForm(this.editForm, feedbackResponder);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      feedbackResponder.employee,
    );
    this.feedbacksSharedCollection = this.feedbackService.addFeedbackToCollectionIfMissing<IFeedback>(
      this.feedbacksSharedCollection,
      feedbackResponder.feedback,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.feedbackResponder?.employee),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.feedbackService
      .query()
      .pipe(map((res: HttpResponse<IFeedback[]>) => res.body ?? []))
      .pipe(
        map((feedbacks: IFeedback[]) =>
          this.feedbackService.addFeedbackToCollectionIfMissing<IFeedback>(feedbacks, this.feedbackResponder?.feedback),
        ),
      )
      .subscribe((feedbacks: IFeedback[]) => (this.feedbacksSharedCollection = feedbacks));
  }
}
