import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRatingScale } from '../rating-scale.model';
import { RatingScaleService } from '../service/rating-scale.service';

@Component({
  standalone: true,
  templateUrl: './rating-scale-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RatingScaleDeleteDialogComponent {
  ratingScale?: IRatingScale;

  protected ratingScaleService = inject(RatingScaleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ratingScaleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
