import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ITeachOther } from '../teach-other.model';

@Component({
  standalone: true,
  selector: 'jhi-teach-other-detail',
  templateUrl: './teach-other-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class TeachOtherDetailComponent {
  teachOther = input<ITeachOther | null>(null);

  previousState(): void {
    window.history.back();
  }
}
