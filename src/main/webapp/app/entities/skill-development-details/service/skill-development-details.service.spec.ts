import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISkillDevelopmentDetails } from '../skill-development-details.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../skill-development-details.test-samples';

import { SkillDevelopmentDetailsService } from './skill-development-details.service';

const requireRestSample: ISkillDevelopmentDetails = {
  ...sampleWithRequiredData,
};

describe('SkillDevelopmentDetails Service', () => {
  let service: SkillDevelopmentDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: ISkillDevelopmentDetails | ISkillDevelopmentDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SkillDevelopmentDetailsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SkillDevelopmentDetails', () => {
      const skillDevelopmentDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(skillDevelopmentDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SkillDevelopmentDetails', () => {
      const skillDevelopmentDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(skillDevelopmentDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SkillDevelopmentDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SkillDevelopmentDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SkillDevelopmentDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSkillDevelopmentDetailsToCollectionIfMissing', () => {
      it('should add a SkillDevelopmentDetails to an empty array', () => {
        const skillDevelopmentDetails: ISkillDevelopmentDetails = sampleWithRequiredData;
        expectedResult = service.addSkillDevelopmentDetailsToCollectionIfMissing([], skillDevelopmentDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skillDevelopmentDetails);
      });

      it('should not add a SkillDevelopmentDetails to an array that contains it', () => {
        const skillDevelopmentDetails: ISkillDevelopmentDetails = sampleWithRequiredData;
        const skillDevelopmentDetailsCollection: ISkillDevelopmentDetails[] = [
          {
            ...skillDevelopmentDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSkillDevelopmentDetailsToCollectionIfMissing(
          skillDevelopmentDetailsCollection,
          skillDevelopmentDetails,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SkillDevelopmentDetails to an array that doesn't contain it", () => {
        const skillDevelopmentDetails: ISkillDevelopmentDetails = sampleWithRequiredData;
        const skillDevelopmentDetailsCollection: ISkillDevelopmentDetails[] = [sampleWithPartialData];
        expectedResult = service.addSkillDevelopmentDetailsToCollectionIfMissing(
          skillDevelopmentDetailsCollection,
          skillDevelopmentDetails,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skillDevelopmentDetails);
      });

      it('should add only unique SkillDevelopmentDetails to an array', () => {
        const skillDevelopmentDetailsArray: ISkillDevelopmentDetails[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const skillDevelopmentDetailsCollection: ISkillDevelopmentDetails[] = [sampleWithRequiredData];
        expectedResult = service.addSkillDevelopmentDetailsToCollectionIfMissing(
          skillDevelopmentDetailsCollection,
          ...skillDevelopmentDetailsArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const skillDevelopmentDetails: ISkillDevelopmentDetails = sampleWithRequiredData;
        const skillDevelopmentDetails2: ISkillDevelopmentDetails = sampleWithPartialData;
        expectedResult = service.addSkillDevelopmentDetailsToCollectionIfMissing([], skillDevelopmentDetails, skillDevelopmentDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skillDevelopmentDetails);
        expect(expectedResult).toContain(skillDevelopmentDetails2);
      });

      it('should accept null and undefined values', () => {
        const skillDevelopmentDetails: ISkillDevelopmentDetails = sampleWithRequiredData;
        expectedResult = service.addSkillDevelopmentDetailsToCollectionIfMissing([], null, skillDevelopmentDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skillDevelopmentDetails);
      });

      it('should return initial array if no SkillDevelopmentDetails is added', () => {
        const skillDevelopmentDetailsCollection: ISkillDevelopmentDetails[] = [sampleWithRequiredData];
        expectedResult = service.addSkillDevelopmentDetailsToCollectionIfMissing(skillDevelopmentDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(skillDevelopmentDetailsCollection);
      });
    });

    describe('compareSkillDevelopmentDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSkillDevelopmentDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSkillDevelopmentDetails(entity1, entity2);
        const compareResult2 = service.compareSkillDevelopmentDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSkillDevelopmentDetails(entity1, entity2);
        const compareResult2 = service.compareSkillDevelopmentDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSkillDevelopmentDetails(entity1, entity2);
        const compareResult2 = service.compareSkillDevelopmentDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
