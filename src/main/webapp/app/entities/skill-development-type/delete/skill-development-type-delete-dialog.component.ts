import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISkillDevelopmentType } from '../skill-development-type.model';
import { SkillDevelopmentTypeService } from '../service/skill-development-type.service';

@Component({
  standalone: true,
  templateUrl: './skill-development-type-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SkillDevelopmentTypeDeleteDialogComponent {
  skillDevelopmentType?: ISkillDevelopmentType;

  protected skillDevelopmentTypeService = inject(SkillDevelopmentTypeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.skillDevelopmentTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
