import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISkillDevelopmentDetails } from '../skill-development-details.model';
import { SkillDevelopmentDetailsService } from '../service/skill-development-details.service';

@Component({
  standalone: true,
  templateUrl: './skill-development-details-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SkillDevelopmentDetailsDeleteDialogComponent {
  skillDevelopmentDetails?: ISkillDevelopmentDetails;

  protected skillDevelopmentDetailsService = inject(SkillDevelopmentDetailsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.skillDevelopmentDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
