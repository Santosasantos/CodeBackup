import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IExtraquestionAns } from '../extraquestion-ans.model';

@Component({
  standalone: true,
  selector: 'jhi-extraquestion-ans-detail',
  templateUrl: './extraquestion-ans-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExtraquestionAnsDetailComponent {
  extraquestionAns = input<IExtraquestionAns | null>(null);

  previousState(): void {
    window.history.back();
  }
}
