import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISkillDevelopmentType } from 'app/entities/skill-development-type/skill-development-type.model';
import { SkillDevelopmentTypeService } from 'app/entities/skill-development-type/service/skill-development-type.service';
import { IFeedbackResponder } from 'app/entities/feedback-responder/feedback-responder.model';
import { FeedbackResponderService } from 'app/entities/feedback-responder/service/feedback-responder.service';
import { SkillDevelopmentDetailsService } from '../service/skill-development-details.service';
import { ISkillDevelopmentDetails } from '../skill-development-details.model';
import { SkillDevelopmentDetailsFormService, SkillDevelopmentDetailsFormGroup } from './skill-development-details-form.service';

@Component({
  standalone: true,
  selector: 'jhi-skill-development-details-update',
  templateUrl: './skill-development-details-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SkillDevelopmentDetailsUpdateComponent implements OnInit {
  isSaving = false;
  skillDevelopmentDetails: ISkillDevelopmentDetails | null = null;

  skillDevelopmentTypesSharedCollection: ISkillDevelopmentType[] = [];
  feedbackRespondersSharedCollection: IFeedbackResponder[] = [];

  protected skillDevelopmentDetailsService = inject(SkillDevelopmentDetailsService);
  protected skillDevelopmentDetailsFormService = inject(SkillDevelopmentDetailsFormService);
  protected skillDevelopmentTypeService = inject(SkillDevelopmentTypeService);
  protected feedbackResponderService = inject(FeedbackResponderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SkillDevelopmentDetailsFormGroup = this.skillDevelopmentDetailsFormService.createSkillDevelopmentDetailsFormGroup();

  compareSkillDevelopmentType = (o1: ISkillDevelopmentType | null, o2: ISkillDevelopmentType | null): boolean =>
    this.skillDevelopmentTypeService.compareSkillDevelopmentType(o1, o2);

  compareFeedbackResponder = (o1: IFeedbackResponder | null, o2: IFeedbackResponder | null): boolean =>
    this.feedbackResponderService.compareFeedbackResponder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ skillDevelopmentDetails }) => {
      this.skillDevelopmentDetails = skillDevelopmentDetails;
      if (skillDevelopmentDetails) {
        this.updateForm(skillDevelopmentDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const skillDevelopmentDetails = this.skillDevelopmentDetailsFormService.getSkillDevelopmentDetails(this.editForm);
    if (skillDevelopmentDetails.id !== null) {
      this.subscribeToSaveResponse(this.skillDevelopmentDetailsService.update(skillDevelopmentDetails));
    } else {
      this.subscribeToSaveResponse(this.skillDevelopmentDetailsService.create(skillDevelopmentDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISkillDevelopmentDetails>>): void {
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

  protected updateForm(skillDevelopmentDetails: ISkillDevelopmentDetails): void {
    this.skillDevelopmentDetails = skillDevelopmentDetails;
    this.skillDevelopmentDetailsFormService.resetForm(this.editForm, skillDevelopmentDetails);

    this.skillDevelopmentTypesSharedCollection =
      this.skillDevelopmentTypeService.addSkillDevelopmentTypeToCollectionIfMissing<ISkillDevelopmentType>(
        this.skillDevelopmentTypesSharedCollection,
        skillDevelopmentDetails.skillDevelopmentType,
      );
    this.feedbackRespondersSharedCollection = this.feedbackResponderService.addFeedbackResponderToCollectionIfMissing<IFeedbackResponder>(
      this.feedbackRespondersSharedCollection,
      skillDevelopmentDetails.responder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.skillDevelopmentTypeService
      .query()
      .pipe(map((res: HttpResponse<ISkillDevelopmentType[]>) => res.body ?? []))
      .pipe(
        map((skillDevelopmentTypes: ISkillDevelopmentType[]) =>
          this.skillDevelopmentTypeService.addSkillDevelopmentTypeToCollectionIfMissing<ISkillDevelopmentType>(
            skillDevelopmentTypes,
            this.skillDevelopmentDetails?.skillDevelopmentType,
          ),
        ),
      )
      .subscribe((skillDevelopmentTypes: ISkillDevelopmentType[]) => (this.skillDevelopmentTypesSharedCollection = skillDevelopmentTypes));

    this.feedbackResponderService
      .query()
      .pipe(map((res: HttpResponse<IFeedbackResponder[]>) => res.body ?? []))
      .pipe(
        map((feedbackResponders: IFeedbackResponder[]) =>
          this.feedbackResponderService.addFeedbackResponderToCollectionIfMissing<IFeedbackResponder>(
            feedbackResponders,
            this.skillDevelopmentDetails?.responder,
          ),
        ),
      )
      .subscribe((feedbackResponders: IFeedbackResponder[]) => (this.feedbackRespondersSharedCollection = feedbackResponders));
  }
}
