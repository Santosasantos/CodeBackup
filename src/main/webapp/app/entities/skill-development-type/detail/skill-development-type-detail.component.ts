import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISkillDevelopmentType } from '../skill-development-type.model';

@Component({
  standalone: true,
  selector: 'jhi-skill-development-type-detail',
  templateUrl: './skill-development-type-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SkillDevelopmentTypeDetailComponent {
  skillDevelopmentType = input<ISkillDevelopmentType | null>(null);

  previousState(): void {
    window.history.back();
  }
}
