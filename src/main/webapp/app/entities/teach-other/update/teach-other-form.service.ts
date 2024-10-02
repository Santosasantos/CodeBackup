import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITeachOther, NewTeachOther } from '../teach-other.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITeachOther for edit and NewTeachOtherFormGroupInput for create.
 */
type TeachOtherFormGroupInput = ITeachOther | PartialWithRequiredKeyOf<NewTeachOther>;

type TeachOtherFormDefaults = Pick<NewTeachOther, 'id'>;

type TeachOtherFormGroupContent = {
  id: FormControl<ITeachOther['id'] | NewTeachOther['id']>;
  technicalSkill: FormControl<ITeachOther['technicalSkill']>;
  recommendation: FormControl<ITeachOther['recommendation']>;
  particularStrengh: FormControl<ITeachOther['particularStrengh']>;
  whynotRecommend: FormControl<ITeachOther['whynotRecommend']>;
  responder: FormControl<ITeachOther['responder']>;
};

export type TeachOtherFormGroup = FormGroup<TeachOtherFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TeachOtherFormService {
  createTeachOtherFormGroup(teachOther: TeachOtherFormGroupInput = { id: null }): TeachOtherFormGroup {
    const teachOtherRawValue = {
      ...this.getFormDefaults(),
      ...teachOther,
    };
    return new FormGroup<TeachOtherFormGroupContent>({
      id: new FormControl(
        { value: teachOtherRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      technicalSkill: new FormControl(teachOtherRawValue.technicalSkill, {
        validators: [Validators.required],
      }),
      recommendation: new FormControl(teachOtherRawValue.recommendation, {
        validators: [Validators.required],
      }),
      particularStrengh: new FormControl(teachOtherRawValue.particularStrengh),
      whynotRecommend: new FormControl(teachOtherRawValue.whynotRecommend),
      responder: new FormControl(teachOtherRawValue.responder),
    });
  }

  getTeachOther(form: TeachOtherFormGroup): ITeachOther | NewTeachOther {
    return form.getRawValue() as ITeachOther | NewTeachOther;
  }

  resetForm(form: TeachOtherFormGroup, teachOther: TeachOtherFormGroupInput): void {
    const teachOtherRawValue = { ...this.getFormDefaults(), ...teachOther };
    form.reset(
      {
        ...teachOtherRawValue,
        id: { value: teachOtherRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TeachOtherFormDefaults {
    return {
      id: null,
    };
  }
}
