import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExtraquestionAns, NewExtraquestionAns } from '../extraquestion-ans.model';

export type PartialUpdateExtraquestionAns = Partial<IExtraquestionAns> & Pick<IExtraquestionAns, 'id'>;

export type EntityResponseType = HttpResponse<IExtraquestionAns>;
export type EntityArrayResponseType = HttpResponse<IExtraquestionAns[]>;

@Injectable({ providedIn: 'root' })
export class ExtraquestionAnsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/extraquestion-ans');

  create(extraquestionAns: NewExtraquestionAns): Observable<EntityResponseType> {
    return this.http.post<IExtraquestionAns>(this.resourceUrl, extraquestionAns, { observe: 'response' });
  }

  update(extraquestionAns: IExtraquestionAns): Observable<EntityResponseType> {
    return this.http.put<IExtraquestionAns>(
      `${this.resourceUrl}/${this.getExtraquestionAnsIdentifier(extraquestionAns)}`,
      extraquestionAns,
      { observe: 'response' },
    );
  }

  partialUpdate(extraquestionAns: PartialUpdateExtraquestionAns): Observable<EntityResponseType> {
    return this.http.patch<IExtraquestionAns>(
      `${this.resourceUrl}/${this.getExtraquestionAnsIdentifier(extraquestionAns)}`,
      extraquestionAns,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExtraquestionAns>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExtraquestionAns[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExtraquestionAnsIdentifier(extraquestionAns: Pick<IExtraquestionAns, 'id'>): number {
    return extraquestionAns.id;
  }

  compareExtraquestionAns(o1: Pick<IExtraquestionAns, 'id'> | null, o2: Pick<IExtraquestionAns, 'id'> | null): boolean {
    return o1 && o2 ? this.getExtraquestionAnsIdentifier(o1) === this.getExtraquestionAnsIdentifier(o2) : o1 === o2;
  }

  addExtraquestionAnsToCollectionIfMissing<Type extends Pick<IExtraquestionAns, 'id'>>(
    extraquestionAnsCollection: Type[],
    ...extraquestionAnsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const extraquestionAns: Type[] = extraquestionAnsToCheck.filter(isPresent);
    if (extraquestionAns.length > 0) {
      const extraquestionAnsCollectionIdentifiers = extraquestionAnsCollection.map(extraquestionAnsItem =>
        this.getExtraquestionAnsIdentifier(extraquestionAnsItem),
      );
      const extraquestionAnsToAdd = extraquestionAns.filter(extraquestionAnsItem => {
        const extraquestionAnsIdentifier = this.getExtraquestionAnsIdentifier(extraquestionAnsItem);
        if (extraquestionAnsCollectionIdentifiers.includes(extraquestionAnsIdentifier)) {
          return false;
        }
        extraquestionAnsCollectionIdentifiers.push(extraquestionAnsIdentifier);
        return true;
      });
      return [...extraquestionAnsToAdd, ...extraquestionAnsCollection];
    }
    return extraquestionAnsCollection;
  }
}
