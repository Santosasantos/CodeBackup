import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackType, NewFeedbackType } from '../feedback-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackType for edit and NewFeedbackTypeFormGroupInput for create.
 */
type FeedbackTypeFormGroupInput = IFeedbackType | PartialWithRequiredKeyOf<NewFeedbackType>;

type FeedbackTypeFormDefaults = Pick<NewFeedbackType, 'id'>;

type FeedbackTypeFormGroupContent = {
  id: FormControl<IFeedbackType['id'] | NewFeedbackType['id']>;
  feedbackname: FormControl<IFeedbackType['feedbackname']>;
};

export type FeedbackTypeFormGroup = FormGroup<FeedbackTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackTypeFormService {
  createFeedbackTypeFormGroup(feedbackType: FeedbackTypeFormGroupInput = { id: null }): FeedbackTypeFormGroup {
    const feedbackTypeRawValue = {
      ...this.getFormDefaults(),
      ...feedbackType,
    };
    return new FormGroup<FeedbackTypeFormGroupContent>({
      id: new FormControl(
        { value: feedbackTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      feedbackname: new FormControl(feedbackTypeRawValue.feedbackname, {
        validators: [Validators.required],
      }),
    });
  }

  getFeedbackType(form: FeedbackTypeFormGroup): IFeedbackType | NewFeedbackType {
    return form.getRawValue() as IFeedbackType | NewFeedbackType;
  }

  resetForm(form: FeedbackTypeFormGroup, feedbackType: FeedbackTypeFormGroupInput): void {
    const feedbackTypeRawValue = { ...this.getFormDefaults(), ...feedbackType };
    form.reset(
      {
        ...feedbackTypeRawValue,
        id: { value: feedbackTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeedbackTypeFormDefaults {
    return {
      id: null,
    };
  }
}
