import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFeedbackType } from '../feedback-type.model';

@Component({
  standalone: true,
  selector: 'jhi-feedback-type-detail',
  templateUrl: './feedback-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FeedbackTypeDetailComponent {
  feedbackType = input<IFeedbackType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
