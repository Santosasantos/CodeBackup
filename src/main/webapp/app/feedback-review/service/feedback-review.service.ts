import { inject, Injectable } from '@angular/core';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { IEmployee } from '../../entities/employee/employee.model';
import { IFeedback } from '../../entities/feedback/feedback.model';
import { ResponderCategory } from '../../entities/enumerations/responder-category.model';

@Injectable({
  providedIn: 'root',
})
export class FeedbackReviewService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected apiEmployee = this.applicationConfigService.getEndpointFor('api/employees');
  protected apiFeedback = this.applicationConfigService.getEndpointFor('api/feedbacks');

  getSuperviseeList(userpin: string): Observable<IEmployee[]> {
    return this.http
      .get<
        IEmployee[]
      >(`${this.apiFeedback}/employees/eligible`, { params: { category: ResponderCategory.SUPERVISEE, currentUserPin: userpin } })
      .pipe(catchError(this.handleError));
  }

  findFeedbackbySuperviseePin(requesterPin: string): Observable<IFeedback> {
    return this.http.get<IFeedback>(`${this.apiFeedback}/requester/${requesterPin}`).pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error('An error occurred:', error);
    return throwError(() => new Error('Something went wrong; please try again later.'));
  }
}
