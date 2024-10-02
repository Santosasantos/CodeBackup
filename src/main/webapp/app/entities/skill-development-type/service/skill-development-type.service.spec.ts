import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISkillDevelopmentType } from '../skill-development-type.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../skill-development-type.test-samples';

import { SkillDevelopmentTypeService } from './skill-development-type.service';

const requireRestSample: ISkillDevelopmentType = {
  ...sampleWithRequiredData,
};

describe('SkillDevelopmentType Service', () => {
  let service: SkillDevelopmentTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ISkillDevelopmentType | ISkillDevelopmentType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SkillDevelopmentTypeService);
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

    it('should create a SkillDevelopmentType', () => {
      const skillDevelopmentType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(skillDevelopmentType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SkillDevelopmentType', () => {
      const skillDevelopmentType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(skillDevelopmentType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SkillDevelopmentType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SkillDevelopmentType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SkillDevelopmentType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSkillDevelopmentTypeToCollectionIfMissing', () => {
      it('should add a SkillDevelopmentType to an empty array', () => {
        const skillDevelopmentType: ISkillDevelopmentType = sampleWithRequiredData;
        expectedResult = service.addSkillDevelopmentTypeToCollectionIfMissing([], skillDevelopmentType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skillDevelopmentType);
      });

      it('should not add a SkillDevelopmentType to an array that contains it', () => {
        const skillDevelopmentType: ISkillDevelopmentType = sampleWithRequiredData;
        const skillDevelopmentTypeCollection: ISkillDevelopmentType[] = [
          {
            ...skillDevelopmentType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSkillDevelopmentTypeToCollectionIfMissing(skillDevelopmentTypeCollection, skillDevelopmentType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SkillDevelopmentType to an array that doesn't contain it", () => {
        const skillDevelopmentType: ISkillDevelopmentType = sampleWithRequiredData;
        const skillDevelopmentTypeCollection: ISkillDevelopmentType[] = [sampleWithPartialData];
        expectedResult = service.addSkillDevelopmentTypeToCollectionIfMissing(skillDevelopmentTypeCollection, skillDevelopmentType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skillDevelopmentType);
      });

      it('should add only unique SkillDevelopmentType to an array', () => {
        const skillDevelopmentTypeArray: ISkillDevelopmentType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const skillDevelopmentTypeCollection: ISkillDevelopmentType[] = [sampleWithRequiredData];
        expectedResult = service.addSkillDevelopmentTypeToCollectionIfMissing(skillDevelopmentTypeCollection, ...skillDevelopmentTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const skillDevelopmentType: ISkillDevelopmentType = sampleWithRequiredData;
        const skillDevelopmentType2: ISkillDevelopmentType = sampleWithPartialData;
        expectedResult = service.addSkillDevelopmentTypeToCollectionIfMissing([], skillDevelopmentType, skillDevelopmentType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(skillDevelopmentType);
        expect(expectedResult).toContain(skillDevelopmentType2);
      });

      it('should accept null and undefined values', () => {
        const skillDevelopmentType: ISkillDevelopmentType = sampleWithRequiredData;
        expectedResult = service.addSkillDevelopmentTypeToCollectionIfMissing([], null, skillDevelopmentType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(skillDevelopmentType);
      });

      it('should return initial array if no SkillDevelopmentType is added', () => {
        const skillDevelopmentTypeCollection: ISkillDevelopmentType[] = [sampleWithRequiredData];
        expectedResult = service.addSkillDevelopmentTypeToCollectionIfMissing(skillDevelopmentTypeCollection, undefined, null);
        expect(expectedResult).toEqual(skillDevelopmentTypeCollection);
      });
    });

    describe('compareSkillDevelopmentType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSkillDevelopmentType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSkillDevelopmentType(entity1, entity2);
        const compareResult2 = service.compareSkillDevelopmentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSkillDevelopmentType(entity1, entity2);
        const compareResult2 = service.compareSkillDevelopmentType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSkillDevelopmentType(entity1, entity2);
        const compareResult2 = service.compareSkillDevelopmentType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
