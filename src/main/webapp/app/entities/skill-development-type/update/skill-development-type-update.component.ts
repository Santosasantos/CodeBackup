import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISkillDevelopmentType } from '../skill-development-type.model';
import { SkillDevelopmentTypeService } from '../service/skill-development-type.service';
import { SkillDevelopmentTypeFormService, SkillDevelopmentTypeFormGroup } from './skill-development-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-skill-development-type-update',
  templateUrl: './skill-development-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SkillDevelopmentTypeUpdateComponent implements OnInit {
  isSaving = false;
  skillDevelopmentType: ISkillDevelopmentType | null = null;

  protected skillDevelopmentTypeService = inject(SkillDevelopmentTypeService);
  protected skillDevelopmentTypeFormService = inject(SkillDevelopmentTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SkillDevelopmentTypeFormGroup = this.skillDevelopmentTypeFormService.createSkillDevelopmentTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ skillDevelopmentType }) => {
      this.skillDevelopmentType = skillDevelopmentType;
      if (skillDevelopmentType) {
        this.updateForm(skillDevelopmentType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const skillDevelopmentType = this.skillDevelopmentTypeFormService.getSkillDevelopmentType(this.editForm);
    if (skillDevelopmentType.id !== null) {
      this.subscribeToSaveResponse(this.skillDevelopmentTypeService.update(skillDevelopmentType));
    } else {
      this.subscribeToSaveResponse(this.skillDevelopmentTypeService.create(skillDevelopmentType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISkillDevelopmentType>>): void {
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

  protected updateForm(skillDevelopmentType: ISkillDevelopmentType): void {
    this.skillDevelopmentType = skillDevelopmentType;
    this.skillDevelopmentTypeFormService.resetForm(this.editForm, skillDevelopmentType);
  }
}
