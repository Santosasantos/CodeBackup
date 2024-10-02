import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRatingScale } from '../rating-scale.model';

@Component({
  standalone: true,
  selector: 'jhi-rating-scale-detail',
  templateUrl: './rating-scale-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RatingScaleDetailComponent {
  ratingScale = input<IRatingScale | null>(null);

  previousState(): void {
    window.history.back();
  }
}
