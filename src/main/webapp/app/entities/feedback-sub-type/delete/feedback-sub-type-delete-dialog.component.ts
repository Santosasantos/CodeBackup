import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFeedbackSubType } from '../feedback-sub-type.model';
import { FeedbackSubTypeService } from '../service/feedback-sub-type.service';

@Component({
  standalone: true,
  templateUrl: './feedback-sub-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FeedbackSubTypeDeleteDialogComponent {
  feedbackSubType?: IFeedbackSubType;

  protected feedbackSubTypeService = inject(FeedbackSubTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feedbackSubTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
