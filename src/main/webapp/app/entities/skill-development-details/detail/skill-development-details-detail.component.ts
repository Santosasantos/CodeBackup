import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISkillDevelopmentDetails } from '../skill-development-details.model';

@Component({
  standalone: true,
  selector: 'jhi-skill-development-details-detail',
  templateUrl: './skill-development-details-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SkillDevelopmentDetailsDetailComponent {
  skillDevelopmentDetails = input<ISkillDevelopmentDetails | null>(null);

  previousState(): void {
    window.history.back();
  }
}
