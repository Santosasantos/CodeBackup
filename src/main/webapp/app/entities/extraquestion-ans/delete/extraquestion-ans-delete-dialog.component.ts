import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IExtraquestionAns } from '../extraquestion-ans.model';
import { ExtraquestionAnsService } from '../service/extraquestion-ans.service';

@Component({
  standalone: true,
  templateUrl: './extraquestion-ans-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ExtraquestionAnsDeleteDialogComponent {
  extraquestionAns?: IExtraquestionAns;

  protected extraquestionAnsService = inject(ExtraquestionAnsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.extraquestionAnsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
