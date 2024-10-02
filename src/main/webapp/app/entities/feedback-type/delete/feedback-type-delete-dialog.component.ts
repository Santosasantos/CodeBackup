import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFeedbackType } from '../feedback-type.model';
import { FeedbackTypeService } from '../service/feedback-type.service';

@Component({
  standalone: true,
  templateUrl: './feedback-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FeedbackTypeDeleteDialogComponent {
  feedbackType?: IFeedbackType;

  protected feedbackTypeService = inject(FeedbackTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
