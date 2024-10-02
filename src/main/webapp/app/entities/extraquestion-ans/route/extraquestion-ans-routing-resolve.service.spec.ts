import { TestBed } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { IExtraquestionAns } from '../extraquestion-ans.model';
import { ExtraquestionAnsService } from '../service/extraquestion-ans.service';

import extraquestionAnsResolve from './extraquestion-ans-routing-resolve.service';

describe('ExtraquestionAns routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ExtraquestionAnsService;
  let resultExtraquestionAns: IExtraquestionAns | null | undefined;

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
    service = TestBed.inject(ExtraquestionAnsService);
    resultExtraquestionAns = undefined;
  });

  describe('resolve', () => {
    it('should return IExtraquestionAns returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        extraquestionAnsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultExtraquestionAns = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultExtraquestionAns).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        extraquestionAnsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultExtraquestionAns = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultExtraquestionAns).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IExtraquestionAns>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        extraquestionAnsResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultExtraquestionAns = result;
          },
        });
      });

      // THEN
      expect(service.find).toHaveBeenCalledWith(123);
      expect(resultExtraquestionAns).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
