import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackDetails, NewFeedbackDetails } from '../feedback-details.model';

export type PartialUpdateFeedbackDetails = Partial<IFeedbackDetails> & Pick<IFeedbackDetails, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackDetails>;
export type EntityArrayResponseType = HttpResponse<IFeedbackDetails[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackDetailsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-details');

  create(feedbackDetails: NewFeedbackDetails): Observable<EntityResponseType> {
    return this.http.post<IFeedbackDetails>(this.resourceUrl, feedbackDetails, { observe: 'response' });
  }

  update(feedbackDetails: IFeedbackDetails): Observable<EntityResponseType> {
    return this.http.put<IFeedbackDetails>(`${this.resourceUrl}/${this.getFeedbackDetailsIdentifier(feedbackDetails)}`, feedbackDetails, {
      observe: 'response',
    });
  }

  partialUpdate(feedbackDetails: PartialUpdateFeedbackDetails): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackDetails>(`${this.resourceUrl}/${this.getFeedbackDetailsIdentifier(feedbackDetails)}`, feedbackDetails, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackDetailsIdentifier(feedbackDetails: Pick<IFeedbackDetails, 'id'>): number {
    return feedbackDetails.id;
  }

  compareFeedbackDetails(o1: Pick<IFeedbackDetails, 'id'> | null, o2: Pick<IFeedbackDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackDetailsIdentifier(o1) === this.getFeedbackDetailsIdentifier(o2) : o1 === o2;
  }

  addFeedbackDetailsToCollectionIfMissing<Type extends Pick<IFeedbackDetails, 'id'>>(
    feedbackDetailsCollection: Type[],
    ...feedbackDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackDetails: Type[] = feedbackDetailsToCheck.filter(isPresent);
    if (feedbackDetails.length > 0) {
      const feedbackDetailsCollectionIdentifiers = feedbackDetailsCollection.map(feedbackDetailsItem =>
        this.getFeedbackDetailsIdentifier(feedbackDetailsItem),
      );
      const feedbackDetailsToAdd = feedbackDetails.filter(feedbackDetailsItem => {
        const feedbackDetailsIdentifier = this.getFeedbackDetailsIdentifier(feedbackDetailsItem);
        if (feedbackDetailsCollectionIdentifiers.includes(feedbackDetailsIdentifier)) {
          return false;
        }
        feedbackDetailsCollectionIdentifiers.push(feedbackDetailsIdentifier);
        return true;
      });
      return [...feedbackDetailsToAdd, ...feedbackDetailsCollection];
    }
    return feedbackDetailsCollection;
  }
}
