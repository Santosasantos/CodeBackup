import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFeedbackDetails, NewFeedbackDetails } from '../feedback-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedbackDetails for edit and NewFeedbackDetailsFormGroupInput for create.
 */
type FeedbackDetailsFormGroupInput = IFeedbackDetails | PartialWithRequiredKeyOf<NewFeedbackDetails>;

type FeedbackDetailsFormDefaults = Pick<NewFeedbackDetails, 'id'>;

type FeedbackDetailsFormGroupContent = {
  id: FormControl<IFeedbackDetails['id'] | NewFeedbackDetails['id']>;
  commentsforfeedbacksubtype: FormControl<IFeedbackDetails['commentsforfeedbacksubtype']>;
  ratingvalue: FormControl<IFeedbackDetails['ratingvalue']>;
  feedbackSubType: FormControl<IFeedbackDetails['feedbackSubType']>;
  responder: FormControl<IFeedbackDetails['responder']>;
};

export type FeedbackDetailsFormGroup = FormGroup<FeedbackDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackDetailsFormService {
  createFeedbackDetailsFormGroup(feedbackDetails: FeedbackDetailsFormGroupInput = { id: null }): FeedbackDetailsFormGroup {
    const feedbackDetailsRawValue = {
      ...this.getFormDefaults(),
      ...feedbackDetails,
    };
    return new FormGroup<FeedbackDetailsFormGroupContent>({
      id: new FormControl(
        { value: feedbackDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      commentsforfeedbacksubtype: new FormControl(feedbackDetailsRawValue.commentsforfeedbacksubtype),
      ratingvalue: new FormControl(feedbackDetailsRawValue.ratingvalue),
      feedbackSubType: new FormControl(feedbackDetailsRawValue.feedbackSubType),
      responder: new FormControl(feedbackDetailsRawValue.responder),
    });
  }

  getFeedbackDetails(form: FeedbackDetailsFormGroup): IFeedbackDetails | NewFeedbackDetails {
    return form.getRawValue() as IFeedbackDetails | NewFeedbackDetails;
  }

  resetForm(form: FeedbackDetailsFormGroup, feedbackDetails: FeedbackDetailsFormGroupInput): void {
    const feedbackDetailsRawValue = { ...this.getFormDefaults(), ...feedbackDetails };
    form.reset(
      {
        ...feedbackDetailsRawValue,
        id: { value: feedbackDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FeedbackDetailsFormDefaults {
    return {
      id: null,
    };
  }
}
