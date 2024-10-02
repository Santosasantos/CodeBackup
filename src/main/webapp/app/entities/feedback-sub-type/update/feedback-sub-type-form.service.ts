import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackSubType, NewFeedbackSubType } from '../feedback-sub-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackSubType for edit and NewFeedbackSubTypeFormGroupInput for create.
 */
type FeedbackSubTypeFormGroupInput = IFeedbackSubType | PartialWithRequiredKeyOf<NewFeedbackSubType>;

type FeedbackSubTypeFormDefaults = Pick<NewFeedbackSubType, 'id'>;

type FeedbackSubTypeFormGroupContent = {
  id: FormControl<IFeedbackSubType['id'] | NewFeedbackSubType['id']>;
  feedbacksubname: FormControl<IFeedbackSubType['feedbacksubname']>;
  feedbackdescription: FormControl<IFeedbackSubType['feedbackdescription']>;
  feedbackType: FormControl<IFeedbackSubType['feedbackType']>;
};

export type FeedbackSubTypeFormGroup = FormGroup<FeedbackSubTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackSubTypeFormService {
  createFeedbackSubTypeFormGroup(feedbackSubType: FeedbackSubTypeFormGroupInput = { id: null }): FeedbackSubTypeFormGroup {
    const feedbackSubTypeRawValue = {
      ...this.getFormDefaults(),
      ...feedbackSubType,
    };
    return new FormGroup<FeedbackSubTypeFormGroupContent>({
      id: new FormControl(
        { value: feedbackSubTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      feedbacksubname: new FormControl(feedbackSubTypeRawValue.feedbacksubname, {
        validators: [Validators.required],
      }),
      feedbackdescription: new FormControl(feedbackSubTypeRawValue.feedbackdescription, {
        validators: [Validators.required],
      }),
      feedbackType: new FormControl(feedbackSubTypeRawValue.feedbackType),
    });
  }

  getFeedbackSubType(form: FeedbackSubTypeFormGroup): IFeedbackSubType | NewFeedbackSubType {
    return form.getRawValue() as IFeedbackSubType | NewFeedbackSubType;
  }

  resetForm(form: FeedbackSubTypeFormGroup, feedbackSubType: FeedbackSubTypeFormGroupInput): void {
    const feedbackSubTypeRawValue = { ...this.getFormDefaults(), ...feedbackSubType };
    form.reset(
      {
        ...feedbackSubTypeRawValue,
        id: { value: feedbackSubTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeedbackSubTypeFormDefaults {
    return {
      id: null,
    };
  }
}
