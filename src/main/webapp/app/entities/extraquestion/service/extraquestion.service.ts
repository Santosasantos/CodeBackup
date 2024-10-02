import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExtraquestion, NewExtraquestion } from '../extraquestion.model';

export type PartialUpdateExtraquestion = Partial<IExtraquestion> & Pick<IExtraquestion, 'id'>;

export type EntityResponseType = HttpResponse<IExtraquestion>;
export type EntityArrayResponseType = HttpResponse<IExtraquestion[]>;

@Injectable({ providedIn: 'root' })
export class ExtraquestionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/extraquestions');

  create(extraquestion: NewExtraquestion): Observable<EntityResponseType> {
    return this.http.post<IExtraquestion>(this.resourceUrl, extraquestion, { observe: 'response' });
  }

  update(extraquestion: IExtraquestion): Observable<EntityResponseType> {
    return this.http.put<IExtraquestion>(`${this.resourceUrl}/${this.getExtraquestionIdentifier(extraquestion)}`, extraquestion, {
      observe: 'response',
    });
  }

  partialUpdate(extraquestion: PartialUpdateExtraquestion): Observable<EntityResponseType> {
    return this.http.patch<IExtraquestion>(`${this.resourceUrl}/${this.getExtraquestionIdentifier(extraquestion)}`, extraquestion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExtraquestion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExtraquestion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getExtraquestionIdentifier(extraquestion: Pick<IExtraquestion, 'id'>): number {
    return extraquestion.id;
  }

  compareExtraquestion(o1: Pick<IExtraquestion, 'id'> | null, o2: Pick<IExtraquestion, 'id'> | null): boolean {
    return o1 && o2 ? this.getExtraquestionIdentifier(o1) === this.getExtraquestionIdentifier(o2) : o1 === o2;
  }

  addExtraquestionToCollectionIfMissing<Type extends Pick<IExtraquestion, 'id'>>(
    extraquestionCollection: Type[],
    ...extraquestionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const extraquestions: Type[] = extraquestionsToCheck.filter(isPresent);
    if (extraquestions.length > 0) {
      const extraquestionCollectionIdentifiers = extraquestionCollection.map(extraquestionItem =>
        this.getExtraquestionIdentifier(extraquestionItem),
      );
      const extraquestionsToAdd = extraquestions.filter(extraquestionItem => {
        const extraquestionIdentifier = this.getExtraquestionIdentifier(extraquestionItem);
        if (extraquestionCollectionIdentifiers.includes(extraquestionIdentifier)) {
          return false;
        }
        extraquestionCollectionIdentifiers.push(extraquestionIdentifier);
        return true;
      });
      return [...extraquestionsToAdd, ...extraquestionCollection];
    }
    return extraquestionCollection;
  }
}
