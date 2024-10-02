import { inject, Injectable } from '@angular/core';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { catchError, map, Observable, throwError } from 'rxjs';
import { IEmployee } from '../../entities/employee/employee.model';
import { FeedbackStatus } from '../../entities/enumerations/feedback-status.model';
import { ResponderCategory } from '../../entities/enumerations/responder-category.model';
import { IFeedback } from '../../entities/feedback/feedback.model';
import { IFeedbackResponder } from '../../entities/feedback-responder/feedback-responder.model';

@Injectable({
  providedIn: 'root',
})
export class FeedbackRequestService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected apiEmployee = this.applicationConfigService.getEndpointFor('api/employees');
  protected apiFeedback = this.applicationConfigService.getEndpointFor('api/feedbacks');
  protected apiFeedbackResponder = this.applicationConfigService.getEndpointFor('api/feedback-responders');

  findByPin(pin: string): Observable<IEmployee> {
    return this.http.get<IEmployee>(`${this.apiEmployee}/pin/${pin}`).pipe(catchError(this.handleError));
  }

  getSupervisor(subordinatePin: string): Observable<IEmployee> {
    return this.http.get<IEmployee>(`${this.apiEmployee}/supervisor/${subordinatePin}`).pipe(catchError(this.handleError));
  }

  searchEmployees(searchTerm: string, category: ResponderCategory, currentuserpin: string): Observable<IEmployee[]> {
    return this.http
      .get<IEmployee[]>(`${this.apiFeedback}/employees/search`, {
        params: { term: searchTerm, category, currentUserPin: currentuserpin },
      })
      .pipe(catchError(this.handleError));
  }

  sendToSupervisor(feedbackId: number): Observable<any> {
    return this.http
      .put(`${this.apiFeedback}/${feedbackId}/update-status`, null, {
        params: { status: FeedbackStatus.SENT_TO_SUPERVISOR },
      })
      .pipe(catchError(this.handleError));
  }

  getrespondersByFeedbackId(feedbackId: number): Observable<IFeedbackResponder[]> {
    return this.http
      .get<IFeedbackResponder[]>(`${this.apiFeedbackResponder}/responderswithfeedback/${feedbackId}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError(() => new Error('Something went wrong; please try again later.'));
  }
}
