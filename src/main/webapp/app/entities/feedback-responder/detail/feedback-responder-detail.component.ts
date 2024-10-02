import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFeedbackResponder } from '../feedback-responder.model';

@Component({
  standalone: true,
  selector: 'jhi-feedback-responder-detail',
  templateUrl: './feedback-responder-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FeedbackResponderDetailComponent {
  feedbackResponder = input<IFeedbackResponder | null>(null);

  previousState(): void {
    window.history.back();
  }
}
