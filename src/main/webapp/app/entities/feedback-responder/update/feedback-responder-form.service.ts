import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackResponder, NewFeedbackResponder } from '../feedback-responder.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackResponder for edit and NewFeedbackResponderFormGroupInput for create.
 */
type FeedbackResponderFormGroupInput = IFeedbackResponder | PartialWithRequiredKeyOf<NewFeedbackResponder>;

type FeedbackResponderFormDefaults = Pick<NewFeedbackResponder, 'id'>;

type FeedbackResponderFormGroupContent = {
  id: FormControl<IFeedbackResponder['id'] | NewFeedbackResponder['id']>;
  category: FormControl<IFeedbackResponder['category']>;
  stakeholderEmail: FormControl<IFeedbackResponder['stakeholderEmail']>;
  responderStatus: FormControl<IFeedbackResponder['responderStatus']>;
  employee: FormControl<IFeedbackResponder['employee']>;
  feedback: FormControl<IFeedbackResponder['feedback']>;
};

export type FeedbackResponderFormGroup = FormGroup<FeedbackResponderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackResponderFormService {
  createFeedbackResponderFormGroup(feedbackResponder: FeedbackResponderFormGroupInput = { id: null }): FeedbackResponderFormGroup {
    const feedbackResponderRawValue = {
      ...this.getFormDefaults(),
      ...feedbackResponder,
    };
    return new FormGroup<FeedbackResponderFormGroupContent>({
      id: new FormControl(
        { value: feedbackResponderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      category: new FormControl(feedbackResponderRawValue.category, {
        validators: [Validators.required],
      }),
      stakeholderEmail: new FormControl(feedbackResponderRawValue.stakeholderEmail),
      responderStatus: new FormControl(feedbackResponderRawValue.responderStatus, {
        validators: [Validators.required],
      }),
      employee: new FormControl(feedbackResponderRawValue.employee),
      feedback: new FormControl(feedbackResponderRawValue.feedback),
    });
  }

  getFeedbackResponder(form: FeedbackResponderFormGroup): IFeedbackResponder | NewFeedbackResponder {
    return form.getRawValue() as IFeedbackResponder | NewFeedbackResponder;
  }

  resetForm(form: FeedbackResponderFormGroup, feedbackResponder: FeedbackResponderFormGroupInput): void {
    const feedbackResponderRawValue = { ...this.getFormDefaults(), ...feedbackResponder };
    form.reset(
      {
        ...feedbackResponderRawValue,
        id: { value: feedbackResponderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeedbackResponderFormDefaults {
    return {
      id: null,
    };
  }
}
