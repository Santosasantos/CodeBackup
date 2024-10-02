import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFeedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';

@Component({
  standalone: true,
  templateUrl: './feedback-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FeedbackDeleteDialogComponent {
  feedback?: IFeedback;

  protected feedbackService = inject(FeedbackService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
