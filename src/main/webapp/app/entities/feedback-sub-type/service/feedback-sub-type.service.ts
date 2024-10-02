import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackSubType, NewFeedbackSubType } from '../feedback-sub-type.model';

export type PartialUpdateFeedbackSubType = Partial<IFeedbackSubType> & Pick<IFeedbackSubType, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackSubType>;
export type EntityArrayResponseType = HttpResponse<IFeedbackSubType[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackSubTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-sub-types');

  create(feedbackSubType: NewFeedbackSubType): Observable<EntityResponseType> {
    return this.http.post<IFeedbackSubType>(this.resourceUrl, feedbackSubType, { observe: 'response' });
  }

  update(feedbackSubType: IFeedbackSubType): Observable<EntityResponseType> {
    return this.http.put<IFeedbackSubType>(`${this.resourceUrl}/${this.getFeedbackSubTypeIdentifier(feedbackSubType)}`, feedbackSubType, {
      observe: 'response',
    });
  }

  partialUpdate(feedbackSubType: PartialUpdateFeedbackSubType): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackSubType>(`${this.resourceUrl}/${this.getFeedbackSubTypeIdentifier(feedbackSubType)}`, feedbackSubType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackSubType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackSubType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackSubTypeIdentifier(feedbackSubType: Pick<IFeedbackSubType, 'id'>): number {
    return feedbackSubType.id;
  }

  compareFeedbackSubType(o1: Pick<IFeedbackSubType, 'id'> | null, o2: Pick<IFeedbackSubType, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackSubTypeIdentifier(o1) === this.getFeedbackSubTypeIdentifier(o2) : o1 === o2;
  }

  addFeedbackSubTypeToCollectionIfMissing<Type extends Pick<IFeedbackSubType, 'id'>>(
    feedbackSubTypeCollection: Type[],
    ...feedbackSubTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackSubTypes: Type[] = feedbackSubTypesToCheck.filter(isPresent);
    if (feedbackSubTypes.length > 0) {
      const feedbackSubTypeCollectionIdentifiers = feedbackSubTypeCollection.map(feedbackSubTypeItem =>
        this.getFeedbackSubTypeIdentifier(feedbackSubTypeItem),
      );
      const feedbackSubTypesToAdd = feedbackSubTypes.filter(feedbackSubTypeItem => {
        const feedbackSubTypeIdentifier = this.getFeedbackSubTypeIdentifier(feedbackSubTypeItem);
        if (feedbackSubTypeCollectionIdentifiers.includes(feedbackSubTypeIdentifier)) {
          return false;
        }
        feedbackSubTypeCollectionIdentifiers.push(feedbackSubTypeIdentifier);
        return true;
      });
      return [...feedbackSubTypesToAdd, ...feedbackSubTypeCollection];
    }
    return feedbackSubTypeCollection;
  }
}
