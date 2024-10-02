import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITeachOther } from '../teach-other.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../teach-other.test-samples';

import { TeachOtherService } from './teach-other.service';

const requireRestSample: ITeachOther = {
  ...sampleWithRequiredData,
};

describe('TeachOther Service', () => {
  let service: TeachOtherService;
  let httpMock: HttpTestingController;
  let expectedResult: ITeachOther | ITeachOther[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TeachOtherService);
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

    it('should create a TeachOther', () => {
      const teachOther = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(teachOther).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TeachOther', () => {
      const teachOther = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(teachOther).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TeachOther', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TeachOther', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TeachOther', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTeachOtherToCollectionIfMissing', () => {
      it('should add a TeachOther to an empty array', () => {
        const teachOther: ITeachOther = sampleWithRequiredData;
        expectedResult = service.addTeachOtherToCollectionIfMissing([], teachOther);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(teachOther);
      });

      it('should not add a TeachOther to an array that contains it', () => {
        const teachOther: ITeachOther = sampleWithRequiredData;
        const teachOtherCollection: ITeachOther[] = [
          {
            ...teachOther,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTeachOtherToCollectionIfMissing(teachOtherCollection, teachOther);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TeachOther to an array that doesn't contain it", () => {
        const teachOther: ITeachOther = sampleWithRequiredData;
        const teachOtherCollection: ITeachOther[] = [sampleWithPartialData];
        expectedResult = service.addTeachOtherToCollectionIfMissing(teachOtherCollection, teachOther);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(teachOther);
      });

      it('should add only unique TeachOther to an array', () => {
        const teachOtherArray: ITeachOther[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const teachOtherCollection: ITeachOther[] = [sampleWithRequiredData];
        expectedResult = service.addTeachOtherToCollectionIfMissing(teachOtherCollection, ...teachOtherArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const teachOther: ITeachOther = sampleWithRequiredData;
        const teachOther2: ITeachOther = sampleWithPartialData;
        expectedResult = service.addTeachOtherToCollectionIfMissing([], teachOther, teachOther2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(teachOther);
        expect(expectedResult).toContain(teachOther2);
      });

      it('should accept null and undefined values', () => {
        const teachOther: ITeachOther = sampleWithRequiredData;
        expectedResult = service.addTeachOtherToCollectionIfMissing([], null, teachOther, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(teachOther);
      });

      it('should return initial array if no TeachOther is added', () => {
        const teachOtherCollection: ITeachOther[] = [sampleWithRequiredData];
        expectedResult = service.addTeachOtherToCollectionIfMissing(teachOtherCollection, undefined, null);
        expect(expectedResult).toEqual(teachOtherCollection);
      });
    });

    describe('compareTeachOther', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTeachOther(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTeachOther(entity1, entity2);
        const compareResult2 = service.compareTeachOther(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTeachOther(entity1, entity2);
        const compareResult2 = service.compareTeachOther(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTeachOther(entity1, entity2);
        const compareResult2 = service.compareTeachOther(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
