import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedbackType, NewFeedbackType } from '../feedback-type.model';

export type PartialUpdateFeedbackType = Partial<IFeedbackType> & Pick<IFeedbackType, 'id'>;

export type EntityResponseType = HttpResponse<IFeedbackType>;
export type EntityArrayResponseType = HttpResponse<IFeedbackType[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedback-types');

  create(feedbackType: NewFeedbackType): Observable<EntityResponseType> {
    return this.http.post<IFeedbackType>(this.resourceUrl, feedbackType, { observe: 'response' });
  }

  update(feedbackType: IFeedbackType): Observable<EntityResponseType> {
    return this.http.put<IFeedbackType>(`${this.resourceUrl}/${this.getFeedbackTypeIdentifier(feedbackType)}`, feedbackType, {
      observe: 'response',
    });
  }

  partialUpdate(feedbackType: PartialUpdateFeedbackType): Observable<EntityResponseType> {
    return this.http.patch<IFeedbackType>(`${this.resourceUrl}/${this.getFeedbackTypeIdentifier(feedbackType)}`, feedbackType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFeedbackType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFeedbackType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackTypeIdentifier(feedbackType: Pick<IFeedbackType, 'id'>): number {
    return feedbackType.id;
  }

  compareFeedbackType(o1: Pick<IFeedbackType, 'id'> | null, o2: Pick<IFeedbackType, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackTypeIdentifier(o1) === this.getFeedbackTypeIdentifier(o2) : o1 === o2;
  }

  addFeedbackTypeToCollectionIfMissing<Type extends Pick<IFeedbackType, 'id'>>(
    feedbackTypeCollection: Type[],
    ...feedbackTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbackTypes: Type[] = feedbackTypesToCheck.filter(isPresent);
    if (feedbackTypes.length > 0) {
      const feedbackTypeCollectionIdentifiers = feedbackTypeCollection.map(feedbackTypeItem =>
        this.getFeedbackTypeIdentifier(feedbackTypeItem),
      );
      const feedbackTypesToAdd = feedbackTypes.filter(feedbackTypeItem => {
        const feedbackTypeIdentifier = this.getFeedbackTypeIdentifier(feedbackTypeItem);
        if (feedbackTypeCollectionIdentifiers.includes(feedbackTypeIdentifier)) {
          return false;
        }
        feedbackTypeCollectionIdentifiers.push(feedbackTypeIdentifier);
        return true;
      });
      return [...feedbackTypesToAdd, ...feedbackTypeCollection];
    }
    return feedbackTypeCollection;
  }
}
