import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRatingScale, NewRatingScale } from '../rating-scale.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRatingScale for edit and NewRatingScaleFormGroupInput for create.
 */
type RatingScaleFormGroupInput = IRatingScale | PartialWithRequiredKeyOf<NewRatingScale>;

type RatingScaleFormDefaults = Pick<NewRatingScale, 'id'>;

type RatingScaleFormGroupContent = {
  id: FormControl<IRatingScale['id'] | NewRatingScale['id']>;
  scaletype: FormControl<IRatingScale['scaletype']>;
  ratingscales: FormControl<IRatingScale['ratingscales']>;
};

export type RatingScaleFormGroup = FormGroup<RatingScaleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RatingScaleFormService {
  createRatingScaleFormGroup(ratingScale: RatingScaleFormGroupInput = { id: null }): RatingScaleFormGroup {
    const ratingScaleRawValue = {
      ...this.getFormDefaults(),
      ...ratingScale,
    };
    return new FormGroup<RatingScaleFormGroupContent>({
      id: new FormControl(
        { value: ratingScaleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      scaletype: new FormControl(ratingScaleRawValue.scaletype, {
        validators: [Validators.required],
      }),
      ratingscales: new FormControl(ratingScaleRawValue.ratingscales, {
        validators: [Validators.required],
      }),
    });
  }

  getRatingScale(form: RatingScaleFormGroup): IRatingScale | NewRatingScale {
    return form.getRawValue() as IRatingScale | NewRatingScale;
  }

  resetForm(form: RatingScaleFormGroup, ratingScale: RatingScaleFormGroupInput): void {
    const ratingScaleRawValue = { ...this.getFormDefaults(), ...ratingScale };
    form.reset(
      {
        ...ratingScaleRawValue,
        id: { value: ratingScaleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RatingScaleFormDefaults {
    return {
      id: null,
    };
  }
}
