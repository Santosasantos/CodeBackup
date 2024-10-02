import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ISkillDevelopmentType } from '../skill-development-type.model';
import { SkillDevelopmentTypeService } from '../service/skill-development-type.service';

import skillDevelopmentTypeResolve from './skill-development-type-routing-resolve.service';

describe('SkillDevelopmentType routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: SkillDevelopmentTypeService;
  let resultSkillDevelopmentType: ISkillDevelopmentType | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(SkillDevelopmentTypeService);
    resultSkillDevelopmentType = undefined;
  });

  describe('resolve', () => {
    it('should return ISkillDevelopmentType returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        skillDevelopmentTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSkillDevelopmentType = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSkillDevelopmentType).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        skillDevelopmentTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSkillDevelopmentType = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultSkillDevelopmentType).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ISkillDevelopmentType>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        skillDevelopmentTypeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultSkillDevelopmentType = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultSkillDevelopmentType).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
