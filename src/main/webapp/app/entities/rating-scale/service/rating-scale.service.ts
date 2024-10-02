import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRatingScale, NewRatingScale } from '../rating-scale.model';

export type PartialUpdateRatingScale = Partial<IRatingScale> & Pick<IRatingScale, 'id'>;

export type EntityResponseType = HttpResponse<IRatingScale>;
export type EntityArrayResponseType = HttpResponse<IRatingScale[]>;

@Injectable({ providedIn: 'root' })
export class RatingScaleService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rating-scales');

  create(ratingScale: NewRatingScale): Observable<EntityResponseType> {
    return this.http.post<IRatingScale>(this.resourceUrl, ratingScale, { observe: 'response' });
  }

  update(ratingScale: IRatingScale): Observable<EntityResponseType> {
    return this.http.put<IRatingScale>(`${this.resourceUrl}/${this.getRatingScaleIdentifier(ratingScale)}`, ratingScale, {
      observe: 'response',
    });
  }

  partialUpdate(ratingScale: PartialUpdateRatingScale): Observable<EntityResponseType> {
    return this.http.patch<IRatingScale>(`${this.resourceUrl}/${this.getRatingScaleIdentifier(ratingScale)}`, ratingScale, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRatingScale>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRatingScale[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRatingScaleIdentifier(ratingScale: Pick<IRatingScale, 'id'>): number {
    return ratingScale.id;
  }

  compareRatingScale(o1: Pick<IRatingScale, 'id'> | null, o2: Pick<IRatingScale, 'id'> | null): boolean {
    return o1 && o2 ? this.getRatingScaleIdentifier(o1) === this.getRatingScaleIdentifier(o2) : o1 === o2;
  }

  addRatingScaleToCollectionIfMissing<Type extends Pick<IRatingScale, 'id'>>(
    ratingScaleCollection: Type[],
    ...ratingScalesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ratingScales: Type[] = ratingScalesToCheck.filter(isPresent);
    if (ratingScales.length > 0) {
      const ratingScaleCollectionIdentifiers = ratingScaleCollection.map(ratingScaleItem => this.getRatingScaleIdentifier(ratingScaleItem));
      const ratingScalesToAdd = ratingScales.filter(ratingScaleItem => {
        const ratingScaleIdentifier = this.getRatingScaleIdentifier(ratingScaleItem);
        if (ratingScaleCollectionIdentifiers.includes(ratingScaleIdentifier)) {
          return false;
        }
        ratingScaleCollectionIdentifiers.push(ratingScaleIdentifier);
        return true;
      });
      return [...ratingScalesToAdd, ...ratingScaleCollection];
    }
    return ratingScaleCollection;
  }
}
