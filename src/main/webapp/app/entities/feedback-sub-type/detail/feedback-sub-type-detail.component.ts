import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFeedbackSubType } from '../feedback-sub-type.model';

@Component({
  standalone: true,
  selector: 'jhi-feedback-sub-type-detail',
  templateUrl: './feedback-sub-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FeedbackSubTypeDetailComponent {
  feedbackSubType = input<IFeedbackSubType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
