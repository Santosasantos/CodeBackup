import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFeedbackResponder } from '../feedback-responder.model';
import { FeedbackResponderService } from '../service/feedback-responder.service';

@Component({
  standalone: true,
  templateUrl: './feedback-responder-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FeedbackResponderDeleteDialogComponent {
  feedbackResponder?: IFeedbackResponder;

  protected feedbackResponderService = inject(FeedbackResponderService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackResponderService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
