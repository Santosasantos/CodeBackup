import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFeedback, NewFeedback } from '../feedback.model';
import { IEmployee } from '../../employee/employee.model';
import { FeedbackStatus } from '../../enumerations/feedback-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFeedback for edit and NewFeedbackFormGroupInput for create.
 */
type FeedbackFormGroupInput = IFeedback | PartialWithRequiredKeyOf<NewFeedback>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFeedback | NewFeedback> = Omit<T, 'requestDate'> & {
  requestDate?: string | null;
};

type FeedbackFormRawValue = FormValueOf<IFeedback>;

type NewFeedbackFormRawValue = FormValueOf<NewFeedback>;

type FeedbackFormDefaults = Pick<NewFeedback, 'id' | 'requestDate'>;

type FeedbackFormGroupContent = {
  id: FormControl<FeedbackFormRawValue['id'] | NewFeedback['id']>;
  requestDate: FormControl<FeedbackFormRawValue['requestDate']>;
  status: FormControl<FeedbackFormRawValue['status']>;
  responseDate: FormControl<FeedbackFormRawValue['responseDate']>;
  createdBy: FormControl<FeedbackFormRawValue['createdBy']>;
  assessmentYear: FormControl<FeedbackFormRawValue['assessmentYear']>;
  requester: FormControl<FeedbackFormRawValue['requester']>;
  ratingScale: FormControl<FeedbackFormRawValue['ratingScale']>;
};

export type FeedbackFormGroup = FormGroup<FeedbackFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FeedbackFormService {
  createFeedbackFormGroup(feedback: FeedbackFormGroupInput = { id: null }): FeedbackFormGroup {
    const feedbackRawValue = this.convertFeedbackToFeedbackRawValue({
      ...this.getFormDefaults(),
      ...feedback,
    });
    return new FormGroup<FeedbackFormGroupContent>({
      id: new FormControl(
        { value: feedbackRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      requestDate: new FormControl(feedbackRawValue.requestDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(feedbackRawValue.status, {
        validators: [Validators.required],
      }),
      responseDate: new FormControl(feedbackRawValue.responseDate),
      createdBy: new FormControl(feedbackRawValue.createdBy, {
        validators: [Validators.required],
      }),
      assessmentYear: new FormControl(feedbackRawValue.assessmentYear, {
        validators: [Validators.required],
      }),
      requester: new FormControl(feedbackRawValue.requester),
      ratingScale: new FormControl(feedbackRawValue.ratingScale),
    });
  }

  getFeedback(form: FeedbackFormGroup): IFeedback | NewFeedback {
    return this.convertFeedbackRawValueToFeedback(form.getRawValue() as FeedbackFormRawValue | NewFeedbackFormRawValue);
  }

  resetForm(form: FeedbackFormGroup, feedback: FeedbackFormGroupInput): void {
    const feedbackRawValue = this.convertFeedbackToFeedbackRawValue({ ...this.getFormDefaults(), ...feedback });
    form.reset(
      {
        ...feedbackRawValue,
        id: { value: feedbackRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  transformToNewFeedback(feedbackForm: FormGroup, requester: IEmployee): NewFeedback {
    const currentDate = new Date();
    const assessmentYear = currentDate.getMonth() >= 6 ? currentDate.getFullYear() : currentDate.getFullYear() - 1;

    return {
      id: null,
      requestDate: dayjs(),
      status: FeedbackStatus.NEW,
      responseDate: null,
      createdBy: requester.pin,
      assessmentYear: assessmentYear,
      requester: requester,
      ratingScale: feedbackForm.get('ratingScale')?.value,
    };
  }

  private getFormDefaults(): FeedbackFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      requestDate: currentTime,
    };
  }

  private convertFeedbackRawValueToFeedback(rawFeedback: FeedbackFormRawValue | NewFeedbackFormRawValue): IFeedback | NewFeedback {
    return {
      ...rawFeedback,
      requestDate: dayjs(rawFeedback.requestDate, DATE_TIME_FORMAT),
    };
  }

  private convertFeedbackToFeedbackRawValue(
    feedback: IFeedback | (Partial<NewFeedback> & FeedbackFormDefaults),
  ): FeedbackFormRawValue | PartialWithRequiredKeyOf<NewFeedbackFormRawValue> {
    return {
      ...feedback,
      requestDate: feedback.requestDate ? feedback.requestDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
