import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IExtraquestion } from '../extraquestion.model';

@Component({
  standalone: true,
  selector: 'jhi-extraquestion-detail',
  templateUrl: './extraquestion-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ExtraquestionDetailComponent {
  extraquestion = input<IExtraquestion | null>(null);

  previousState(): void {
    window.history.back();
  }
}
