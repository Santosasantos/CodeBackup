import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISkillDevelopmentDetails, NewSkillDevelopmentDetails } from '../skill-development-details.model';

export type PartialUpdateSkillDevelopmentDetails = Partial<ISkillDevelopmentDetails> & Pick<ISkillDevelopmentDetails, 'id'>;

export type EntityResponseType = HttpResponse<ISkillDevelopmentDetails>;
export type EntityArrayResponseType = HttpResponse<ISkillDevelopmentDetails[]>;

@Injectable({ providedIn: 'root' })
export class SkillDevelopmentDetailsService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/skill-development-details');

  create(skillDevelopmentDetails: NewSkillDevelopmentDetails): Observable<EntityResponseType> {
    return this.http.post<ISkillDevelopmentDetails>(this.resourceUrl, skillDevelopmentDetails, { observe: 'response' });
  }

  update(skillDevelopmentDetails: ISkillDevelopmentDetails): Observable<EntityResponseType> {
    return this.http.put<ISkillDevelopmentDetails>(
      `${this.resourceUrl}/${this.getSkillDevelopmentDetailsIdentifier(skillDevelopmentDetails)}`,
      skillDevelopmentDetails,
      { observe: 'response' },
    );
  }

  partialUpdate(skillDevelopmentDetails: PartialUpdateSkillDevelopmentDetails): Observable<EntityResponseType> {
    return this.http.patch<ISkillDevelopmentDetails>(
      `${this.resourceUrl}/${this.getSkillDevelopmentDetailsIdentifier(skillDevelopmentDetails)}`,
      skillDevelopmentDetails,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISkillDevelopmentDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISkillDevelopmentDetails[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSkillDevelopmentDetailsIdentifier(skillDevelopmentDetails: Pick<ISkillDevelopmentDetails, 'id'>): number {
    return skillDevelopmentDetails.id;
  }

  compareSkillDevelopmentDetails(
    o1: Pick<ISkillDevelopmentDetails, 'id'> | null,
    o2: Pick<ISkillDevelopmentDetails, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getSkillDevelopmentDetailsIdentifier(o1) === this.getSkillDevelopmentDetailsIdentifier(o2) : o1 === o2;
  }

  addSkillDevelopmentDetailsToCollectionIfMissing<Type extends Pick<ISkillDevelopmentDetails, 'id'>>(
    skillDevelopmentDetailsCollection: Type[],
    ...skillDevelopmentDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const skillDevelopmentDetails: Type[] = skillDevelopmentDetailsToCheck.filter(isPresent);
    if (skillDevelopmentDetails.length > 0) {
      const skillDevelopmentDetailsCollectionIdentifiers = skillDevelopmentDetailsCollection.map(skillDevelopmentDetailsItem =>
        this.getSkillDevelopmentDetailsIdentifier(skillDevelopmentDetailsItem),
      );
      const skillDevelopmentDetailsToAdd = skillDevelopmentDetails.filter(skillDevelopmentDetailsItem => {
        const skillDevelopmentDetailsIdentifier = this.getSkillDevelopmentDetailsIdentifier(skillDevelopmentDetailsItem);
        if (skillDevelopmentDetailsCollectionIdentifiers.includes(skillDevelopmentDetailsIdentifier)) {
          return false;
        }
        skillDevelopmentDetailsCollectionIdentifiers.push(skillDevelopmentDetailsIdentifier);
        return true;
      });
      return [...skillDevelopmentDetailsToAdd, ...skillDevelopmentDetailsCollection];
    }
    return skillDevelopmentDetailsCollection;
  }
}
