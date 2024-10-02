import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeedback, NewFeedback } from '../feedback.model';

export type PartialUpdateFeedback = Partial<IFeedback> & Pick<IFeedback, 'id'>;

type RestOf<T extends IFeedback | NewFeedback> = Omit<T, 'requestDate' | 'responseDate'> & {
  requestDate?: string | null;
  responseDate?: string | null;
};

export type RestFeedback = RestOf<IFeedback>;

export type NewRestFeedback = RestOf<NewFeedback>;

export type PartialUpdateRestFeedback = RestOf<PartialUpdateFeedback>;

export type EntityResponseType = HttpResponse<IFeedback>;
export type EntityArrayResponseType = HttpResponse<IFeedback[]>;

@Injectable({ providedIn: 'root' })
export class FeedbackService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/feedbacks');

  create(feedback: NewFeedback): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedback);
    return this.http
      .post<RestFeedback>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(feedback: IFeedback): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedback);
    return this.http
      .put<RestFeedback>(`${this.resourceUrl}/${this.getFeedbackIdentifier(feedback)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(feedback: PartialUpdateFeedback): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feedback);
    return this.http
      .patch<RestFeedback>(`${this.resourceUrl}/${this.getFeedbackIdentifier(feedback)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFeedback>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFeedback[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeedbackIdentifier(feedback: Pick<IFeedback, 'id'>): number {
    return feedback.id;
  }

  compareFeedback(o1: Pick<IFeedback, 'id'> | null, o2: Pick<IFeedback, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeedbackIdentifier(o1) === this.getFeedbackIdentifier(o2) : o1 === o2;
  }

  addFeedbackToCollectionIfMissing<Type extends Pick<IFeedback, 'id'>>(
    feedbackCollection: Type[],
    ...feedbacksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feedbacks: Type[] = feedbacksToCheck.filter(isPresent);
    if (feedbacks.length > 0) {
      const feedbackCollectionIdentifiers = feedbackCollection.map(feedbackItem => this.getFeedbackIdentifier(feedbackItem));
      const feedbacksToAdd = feedbacks.filter(feedbackItem => {
        const feedbackIdentifier = this.getFeedbackIdentifier(feedbackItem);
        if (feedbackCollectionIdentifiers.includes(feedbackIdentifier)) {
          return false;
        }
        feedbackCollectionIdentifiers.push(feedbackIdentifier);
        return true;
      });
      return [...feedbacksToAdd, ...feedbackCollection];
    }
    return feedbackCollection;
  }

  protected convertDateFromClient<T extends IFeedback | NewFeedback | PartialUpdateFeedback>(feedback: T): RestOf<T> {
    return {
      ...feedback,
      requestDate: feedback.requestDate?.toJSON() ?? null,
      responseDate: feedback.responseDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFeedback: RestFeedback): IFeedback {
    return {
      ...restFeedback,
      requestDate: restFeedback.requestDate ? dayjs(restFeedback.requestDate) : undefined,
      responseDate: restFeedback.responseDate ? dayjs(restFeedback.responseDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFeedback>): HttpResponse<IFeedback> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFeedback[]>): HttpResponse<IFeedback[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
