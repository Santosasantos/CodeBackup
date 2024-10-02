import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExtraquestionAns, NewExtraquestionAns } from '../extraquestion-ans.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExtraquestionAns for edit and NewExtraquestionAnsFormGroupInput for create.
 */
type ExtraquestionAnsFormGroupInput = IExtraquestionAns | PartialWithRequiredKeyOf<NewExtraquestionAns>;

type ExtraquestionAnsFormDefaults = Pick<NewExtraquestionAns, 'id'>;

type ExtraquestionAnsFormGroupContent = {
  id: FormControl<IExtraquestionAns['id'] | NewExtraquestionAns['id']>;
  questionans: FormControl<IExtraquestionAns['questionans']>;
  questions: FormControl<IExtraquestionAns['questions']>;
};

export type ExtraquestionAnsFormGroup = FormGroup<ExtraquestionAnsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExtraquestionAnsFormService {
  createExtraquestionAnsFormGroup(extraquestionAns: ExtraquestionAnsFormGroupInput = { id: null }): ExtraquestionAnsFormGroup {
    const extraquestionAnsRawValue = {
      ...this.getFormDefaults(),
      ...extraquestionAns,
    };
    return new FormGroup<ExtraquestionAnsFormGroupContent>({
      id: new FormControl(
        { value: extraquestionAnsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      questionans: new FormControl(extraquestionAnsRawValue.questionans, {
        validators: [Validators.required],
      }),
      questions: new FormControl(extraquestionAnsRawValue.questions),
    });
  }

  getExtraquestionAns(form: ExtraquestionAnsFormGroup): IExtraquestionAns | NewExtraquestionAns {
    return form.getRawValue() as IExtraquestionAns | NewExtraquestionAns;
  }

  resetForm(form: ExtraquestionAnsFormGroup, extraquestionAns: ExtraquestionAnsFormGroupInput): void {
    const extraquestionAnsRawValue = { ...this.getFormDefaults(), ...extraquestionAns };
    form.reset(
      {
        ...extraquestionAnsRawValue,
        id: { value: extraquestionAnsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExtraquestionAnsFormDefaults {
    return {
      id: null,
    };
  }
}
