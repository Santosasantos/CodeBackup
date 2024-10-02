import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISkillDevelopmentType, NewSkillDevelopmentType } from '../skill-development-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISkillDevelopmentType for edit and NewSkillDevelopmentTypeFormGroupInput for create.
 */
type SkillDevelopmentTypeFormGroupInput = ISkillDevelopmentType | PartialWithRequiredKeyOf<NewSkillDevelopmentType>;

type SkillDevelopmentTypeFormDefaults = Pick<NewSkillDevelopmentType, 'id'>;

type SkillDevelopmentTypeFormGroupContent = {
  id: FormControl<ISkillDevelopmentType['id'] | NewSkillDevelopmentType['id']>;
  skilldevelopmentname: FormControl<ISkillDevelopmentType['skilldevelopmentname']>;
};

export type SkillDevelopmentTypeFormGroup = FormGroup<SkillDevelopmentTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SkillDevelopmentTypeFormService {
  createSkillDevelopmentTypeFormGroup(
    skillDevelopmentType: SkillDevelopmentTypeFormGroupInput = { id: null },
  ): SkillDevelopmentTypeFormGroup {
    const skillDevelopmentTypeRawValue = {
      ...this.getFormDefaults(),
      ...skillDevelopmentType,
    };
    return new FormGroup<SkillDevelopmentTypeFormGroupContent>({
      id: new FormControl(
        { value: skillDevelopmentTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      skilldevelopmentname: new FormControl(skillDevelopmentTypeRawValue.skilldevelopmentname, {
        validators: [Validators.required],
      }),
    });
  }

  getSkillDevelopmentType(form: SkillDevelopmentTypeFormGroup): ISkillDevelopmentType | NewSkillDevelopmentType {
    return form.getRawValue() as ISkillDevelopmentType | NewSkillDevelopmentType;
  }

  resetForm(form: SkillDevelopmentTypeFormGroup, skillDevelopmentType: SkillDevelopmentTypeFormGroupInput): void {
    const skillDevelopmentTypeRawValue = { ...this.getFormDefaults(), ...skillDevelopmentType };
    form.reset(
      {
        ...skillDevelopmentTypeRawValue,
        id: { value: skillDevelopmentTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SkillDevelopmentTypeFormDefaults {
    return {
      id: null,
    };
  }
}
