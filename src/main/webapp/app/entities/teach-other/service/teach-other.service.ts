import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeachOther, NewTeachOther } from '../teach-other.model';

export type PartialUpdateTeachOther = Partial<ITeachOther> & Pick<ITeachOther, 'id'>;

export type EntityResponseType = HttpResponse<ITeachOther>;
export type EntityArrayResponseType = HttpResponse<ITeachOther[]>;

@Injectable({ providedIn: 'root' })
export class TeachOtherService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/teach-others');

  create(teachOther: NewTeachOther): Observable<EntityResponseType> {
    return this.http.post<ITeachOther>(this.resourceUrl, teachOther, { observe: 'response' });
  }

  update(teachOther: ITeachOther): Observable<EntityResponseType> {
    return this.http.put<ITeachOther>(`${this.resourceUrl}/${this.getTeachOtherIdentifier(teachOther)}`, teachOther, {
      observe: 'response',
    });
  }

  partialUpdate(teachOther: PartialUpdateTeachOther): Observable<EntityResponseType> {
    return this.http.patch<ITeachOther>(`${this.resourceUrl}/${this.getTeachOtherIdentifier(teachOther)}`, teachOther, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeachOther>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeachOther[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTeachOtherIdentifier(teachOther: Pick<ITeachOther, 'id'>): number {
    return teachOther.id;
  }

  compareTeachOther(o1: Pick<ITeachOther, 'id'> | null, o2: Pick<ITeachOther, 'id'> | null): boolean {
    return o1 && o2 ? this.getTeachOtherIdentifier(o1) === this.getTeachOtherIdentifier(o2) : o1 === o2;
  }

  addTeachOtherToCollectionIfMissing<Type extends Pick<ITeachOther, 'id'>>(
    teachOtherCollection: Type[],
    ...teachOthersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const teachOthers: Type[] = teachOthersToCheck.filter(isPresent);
    if (teachOthers.length > 0) {
      const teachOtherCollectionIdentifiers = teachOtherCollection.map(teachOtherItem => this.getTeachOtherIdentifier(teachOtherItem));
      const teachOthersToAdd = teachOthers.filter(teachOtherItem => {
        const teachOtherIdentifier = this.getTeachOtherIdentifier(teachOtherItem);
        if (teachOtherCollectionIdentifiers.includes(teachOtherIdentifier)) {
          return false;
        }
        teachOtherCollectionIdentifiers.push(teachOtherIdentifier);
        return true;
      });
      return [...teachOthersToAdd, ...teachOtherCollection];
    }
    return teachOtherCollection;
  }
}
