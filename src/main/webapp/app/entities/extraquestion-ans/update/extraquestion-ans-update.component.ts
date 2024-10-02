import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IExtraquestion } from 'app/entities/extraquestion/extraquestion.model';
import { ExtraquestionService } from 'app/entities/extraquestion/service/extraquestion.service';
import { IExtraquestionAns } from '../extraquestion-ans.model';
import { ExtraquestionAnsService } from '../service/extraquestion-ans.service';
import { ExtraquestionAnsFormService, ExtraquestionAnsFormGroup } from './extraquestion-ans-form.service';

@Component({
  standalone: true,
  selector: 'jhi-extraquestion-ans-update',
  templateUrl: './extraquestion-ans-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExtraquestionAnsUpdateComponent implements OnInit {
  isSaving = false;
  extraquestionAns: IExtraquestionAns | null = null;

  extraquestionsSharedCollection: IExtraquestion[] = [];

  protected extraquestionAnsService = inject(ExtraquestionAnsService);
  protected extraquestionAnsFormService = inject(ExtraquestionAnsFormService);
  protected extraquestionService = inject(ExtraquestionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExtraquestionAnsFormGroup = this.extraquestionAnsFormService.createExtraquestionAnsFormGroup();

  compareExtraquestion = (o1: IExtraquestion | null, o2: IExtraquestion | null): boolean =>
    this.extraquestionService.compareExtraquestion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ extraquestionAns }) => {
      this.extraquestionAns = extraquestionAns;
      if (extraquestionAns) {
        this.updateForm(extraquestionAns);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const extraquestionAns = this.extraquestionAnsFormService.getExtraquestionAns(this.editForm);
    if (extraquestionAns.id !== null) {
      this.subscribeToSaveResponse(this.extraquestionAnsService.update(extraquestionAns));
    } else {
      this.subscribeToSaveResponse(this.extraquestionAnsService.create(extraquestionAns));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExtraquestionAns>>): void {
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

  protected updateForm(extraquestionAns: IExtraquestionAns): void {
    this.extraquestionAns = extraquestionAns;
    this.extraquestionAnsFormService.resetForm(this.editForm, extraquestionAns);

    this.extraquestionsSharedCollection = this.extraquestionService.addExtraquestionToCollectionIfMissing<IExtraquestion>(
      this.extraquestionsSharedCollection,
      extraquestionAns.questions,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.extraquestionService
      .query()
      .pipe(map((res: HttpResponse<IExtraquestion[]>) => res.body ?? []))
      .pipe(
        map((extraquestions: IExtraquestion[]) =>
          this.extraquestionService.addExtraquestionToCollectionIfMissing<IExtraquestion>(extraquestions, this.extraquestionAns?.questions),
        ),
      )
      .subscribe((extraquestions: IExtraquestion[]) => (this.extraquestionsSharedCollection = extraquestions));
  }
}
