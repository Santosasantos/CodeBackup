import { Component, OnInit, ChangeDetectorRef, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { forkJoin, Observable, of, Subscription } from 'rxjs';
import { map, finalize, switchMap, catchError } from 'rxjs/operators';
import { IFeedback } from '../../entities/feedback/feedback.model';
import { FeedbackStatus } from '../../entities/enumerations/feedback-status.model';
import { IEmployee } from '../../entities/employee/employee.model';
import { FeedbackReviewService } from '../service/feedback-review.service';

@Component({
  selector: 'jhi-feedback-supervisee-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './feedback-supervisee-list.component.html',
  styleUrl: './feedback-supervisee-list.component.scss',
})
export class FeedbackSuperviseeListComponent implements OnInit, OnDestroy {
  superviseeMap: Map<IEmployee, IFeedback | null> = new Map();
  isLoading = false;
  private subscription: Subscription = new Subscription();

  constructor(
    private feedbackReviewService: FeedbackReviewService,
    private router: Router,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadSuperviseeData();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  loadSuperviseeData(): void {
    this.isLoading = true;
    this.subscription.add(
      this.feedbackReviewService
        .getSuperviseeList('1002')
        .pipe(
          switchMap((supervisees: IEmployee[]) => {
            console.log('supervisees:', supervisees);
            if (supervisees.length === 0) {
              return of([]);
            }
            const feedbackRequests: Observable<IFeedback | null>[] = supervisees.map(supervisee =>
              this.feedbackReviewService.findFeedbackbySuperviseePin(supervisee.pin!).pipe(catchError(() => of(null))),
            );
            return forkJoin(feedbackRequests).pipe(
              map((feedbacks: (IFeedback | null)[]) => {
                return supervisees.map((supervisee, index) => ({
                  supervisee,
                  feedback: feedbacks[index],
                }));
              }),
            );
          }),
          finalize(() => {
            this.isLoading = false;
            this.cdr.detectChanges();
          }),
        )
        .subscribe(
          result => {
            this.superviseeMap.clear();
            result.forEach(({ supervisee, feedback }) => {
              this.superviseeMap.set(supervisee, feedback);
            });
            console.log('Supervisee Map:', this.superviseeMap);
          },
          error => {
            console.error('Error loading supervisee data:', error);
            this.isLoading = false;
            this.cdr.detectChanges();
          },
        ),
    );
  }

  isButtonDisabled(feedback: IFeedback | null): boolean {
    return !feedback || (feedback.status !== FeedbackStatus.COMPLETED && feedback.status !== FeedbackStatus.APPROVED);
  }

  viewFeedback(feedbackId: number | undefined): void {
    if (feedbackId) {
      this.router.navigate(['/feedback', feedbackId]).catch(error => {
        console.error('Navigation error:', error);
        // Handle navigation error (e.g., show a message to the user)
      });
    } else {
      console.warn('Attempted to view feedback with undefined id');
      // Maybe show a message to the user
    }
  }

  getFeedbackStatus(feedback: IFeedback | null): string {
    console.log('Feedback:', feedback);
    if (!feedback) return 'No feedback';
    else return feedback.status || 'Unknown';
  }

  get isMapEmpty(): boolean {
    return this.superviseeMap.size === 0;
  }

  protected readonly FeedbackStatus = FeedbackStatus;
}
