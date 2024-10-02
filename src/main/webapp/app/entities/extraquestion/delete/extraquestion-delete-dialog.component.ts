import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExtraquestion } from '../extraquestion.model';
import { ExtraquestionService } from '../service/extraquestion.service';

@Component({
  standalone: true,
  templateUrl: './extraquestion-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExtraquestionDeleteDialogComponent {
  extraquestion?: IExtraquestion;

  protected extraquestionService = inject(ExtraquestionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.extraquestionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
