import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISkillDevelopmentType, NewSkillDevelopmentType } from '../skill-development-type.model';

export type PartialUpdateSkillDevelopmentType = Partial<ISkillDevelopmentType> & Pick<ISkillDevelopmentType, 'id'>;

export type EntityResponseType = HttpResponse<ISkillDevelopmentType>;
export type EntityArrayResponseType = HttpResponse<ISkillDevelopmentType[]>;

@Injectable({ providedIn: 'root' })
export class SkillDevelopmentTypeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/skill-development-types');

  create(skillDevelopmentType: NewSkillDevelopmentType): Observable<EntityResponseType> {
    return this.http.post<ISkillDevelopmentType>(this.resourceUrl, skillDevelopmentType, { observe: 'response' });
  }

  update(skillDevelopmentType: ISkillDevelopmentType): Observable<EntityResponseType> {
    return this.http.put<ISkillDevelopmentType>(
      `${this.resourceUrl}/${this.getSkillDevelopmentTypeIdentifier(skillDevelopmentType)}`,
      skillDevelopmentType,
      { observe: 'response' },
    );
  }

  partialUpdate(skillDevelopmentType: PartialUpdateSkillDevelopmentType): Observable<EntityResponseType> {
    return this.http.patch<ISkillDevelopmentType>(
      `${this.resourceUrl}/${this.getSkillDevelopmentTypeIdentifier(skillDevelopmentType)}`,
      skillDevelopmentType,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISkillDevelopmentType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISkillDevelopmentType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSkillDevelopmentTypeIdentifier(skillDevelopmentType: Pick<ISkillDevelopmentType, 'id'>): number {
    return skillDevelopmentType.id;
  }

  compareSkillDevelopmentType(o1: Pick<ISkillDevelopmentType, 'id'> | null, o2: Pick<ISkillDevelopmentType, 'id'> | null): boolean {
    return o1 && o2 ? this.getSkillDevelopmentTypeIdentifier(o1) === this.getSkillDevelopmentTypeIdentifier(o2) : o1 === o2;
  }

  addSkillDevelopmentTypeToCollectionIfMissing<Type extends Pick<ISkillDevelopmentType, 'id'>>(
    skillDevelopmentTypeCollection: Type[],
    ...skillDevelopmentTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const skillDevelopmentTypes: Type[] = skillDevelopmentTypesToCheck.filter(isPresent);
    if (skillDevelopmentTypes.length > 0) {
      const skillDevelopmentTypeCollectionIdentifiers = skillDevelopmentTypeCollection.map(skillDevelopmentTypeItem =>
        this.getSkillDevelopmentTypeIdentifier(skillDevelopmentTypeItem),
      );
      const skillDevelopmentTypesToAdd = skillDevelopmentTypes.filter(skillDevelopmentTypeItem => {
        const skillDevelopmentTypeIdentifier = this.getSkillDevelopmentTypeIdentifier(skillDevelopmentTypeItem);
        if (skillDevelopmentTypeCollectionIdentifiers.includes(skillDevelopmentTypeIdentifier)) {
          return false;
        }
        skillDevelopmentTypeCollectionIdentifiers.push(skillDevelopmentTypeIdentifier);
        return true;
      });
      return [...skillDevelopmentTypesToAdd, ...skillDevelopmentTypeCollection];
    }
    return skillDevelopmentTypeCollection;
  }
}
