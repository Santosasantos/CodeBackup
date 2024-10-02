import { Component, OnInit } from '@angular/core';
import { IEmployee } from '../entities/employee/employee.model';
import { FeedbackReviewService } from './service/feedback-review.service';
import { IFeedback } from '../entities/feedback/feedback.model';

@Component({
  selector: 'jhi-feedback-review',
  standalone: true,
  imports: [],
  templateUrl: './feedback-review.component.html',
  styleUrl: './feedback-review.component.scss',
})
export class FeedbackReviewComponent implements OnInit {
  superviseeList: IEmployee[] = [];
  feedback!: IFeedback;
  constructor(private feedbackReviewService: FeedbackReviewService) {}

  ngOnInit(): void {
    // this.findSuperviseeList();
  }
  // findSuperviseeList(): void {
  //   this.feedbackReviewService.getSuperviseeList().subscribe((res: IEmployee[]) => {
  //     this.superviseeList = res;
  //     });
  // }
  findFeedbackBySuperviseePin(requesterPin: string): void {
    this.feedbackReviewService.findFeedbackbySuperviseePin(requesterPin).subscribe((res: IFeedback) => {
      this.feedback = res;
    });
  }
}
