import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IExtraquestion, NewExtraquestion } from '../extraquestion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExtraquestion for edit and NewExtraquestionFormGroupInput for create.
 */
type ExtraquestionFormGroupInput = IExtraquestion | PartialWithRequiredKeyOf<NewExtraquestion>;

type ExtraquestionFormDefaults = Pick<NewExtraquestion, 'id'>;

type ExtraquestionFormGroupContent = {
  id: FormControl<IExtraquestion['id'] | NewExtraquestion['id']>;
  question: FormControl<IExtraquestion['question']>;
  feedback: FormControl<IExtraquestion['feedback']>;
};

export type ExtraquestionFormGroup = FormGroup<ExtraquestionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExtraquestionFormService {
  createExtraquestionFormGroup(extraquestion: ExtraquestionFormGroupInput = { id: null }): ExtraquestionFormGroup {
    const extraquestionRawValue = {
      ...this.getFormDefaults(),
      ...extraquestion,
    };
    return new FormGroup<ExtraquestionFormGroupContent>({
      id: new FormControl(
        { value: extraquestionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      question: new FormControl(extraquestionRawValue.question, {
        validators: [Validators.required],
      }),
      feedback: new FormControl(extraquestionRawValue.feedback),
    });
  }

  getExtraquestion(form: ExtraquestionFormGroup): IExtraquestion | NewExtraquestion {
    return form.getRawValue() as IExtraquestion | NewExtraquestion;
  }

  resetForm(form: ExtraquestionFormGroup, extraquestion: ExtraquestionFormGroupInput): void {
    const extraquestionRawValue = { ...this.getFormDefaults(), ...extraquestion };
    form.reset(
      {
        ...extraquestionRawValue,
        id: { value: extraquestionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExtraquestionFormDefaults {
    return {
      id: null,
    };
  }
}
