import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackResponder, NewFeedbackResponder } from '../feedback-responder.model';

export type PartialUpdateFeedbackResponder = Partial<IFeedbackResponder> & Pick<IFeedbackResponder, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackResponder>;
export type EntityArrayResponseType = HttpResponse<IFeedbackResponder[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackResponderService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-responders');

  create(feedbackResponder: NewFeedbackResponder): Observable<EntityResponseType> {
    return this.http.post<IFeedbackResponder>(this.resourceUrl, feedbackResponder, { observe: 'response' });
  }

  update(feedbackResponder: IFeedbackResponder): Observable<EntityResponseType> {
    return this.http.put<IFeedbackResponder>(
      `${this.resourceUrl}/${this.getFeedbackResponderIdentifier(feedbackResponder)}`,
      feedbackResponder,
      { observe: 'response' },
    );
  }

  partialUpdate(feedbackResponder: PartialUpdateFeedbackResponder): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackResponder>(
      `${this.resourceUrl}/${this.getFeedbackResponderIdentifier(feedbackResponder)}`,
      feedbackResponder,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackResponder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackResponder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackResponderIdentifier(feedbackResponder: Pick<IFeedbackResponder, 'id'>): number {
    return feedbackResponder.id;
  }

  compareFeedbackResponder(o1: Pick<IFeedbackResponder, 'id'> | null, o2: Pick<IFeedbackResponder, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackResponderIdentifier(o1) === this.getFeedbackResponderIdentifier(o2) : o1 === o2;
  }

  addFeedbackResponderToCollectionIfMissing<Type extends Pick<IFeedbackResponder, 'id'>>(
    feedbackResponderCollection: Type[],
    ...feedbackRespondersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackResponders: Type[] = feedbackRespondersToCheck.filter(isPresent);
    if (feedbackResponders.length > 0) {
      const feedbackResponderCollectionIdentifiers = feedbackResponderCollection.map(feedbackResponderItem =>
        this.getFeedbackResponderIdentifier(feedbackResponderItem),
      );
      const feedbackRespondersToAdd = feedbackResponders.filter(feedbackResponderItem => {
        const feedbackResponderIdentifier = this.getFeedbackResponderIdentifier(feedbackResponderItem);
        if (feedbackResponderCollectionIdentifiers.includes(feedbackResponderIdentifier)) {
          return false;
        }
        feedbackResponderCollectionIdentifiers.push(feedbackResponderIdentifier);
        return true;
      });
      return [...feedbackRespondersToAdd, ...feedbackResponderCollection];
    }
    return feedbackResponderCollection;
  }
}
