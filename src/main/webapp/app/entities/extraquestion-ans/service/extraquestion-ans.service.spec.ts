import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExtraquestionAns } from '../extraquestion-ans.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../extraquestion-ans.test-samples';

import { ExtraquestionAnsService } from './extraquestion-ans.service';

const requireRestSample: IExtraquestionAns = {
  ...sampleWithRequiredData,
};

describe('ExtraquestionAns Service', () => {
  let service: ExtraquestionAnsService;
  let httpMock: HttpTestingController;
  let expectedResult: IExtraquestionAns | IExtraquestionAns[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExtraquestionAnsService);
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

    it('should create a ExtraquestionAns', () => {
      const extraquestionAns = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(extraquestionAns).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExtraquestionAns', () => {
      const extraquestionAns = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(extraquestionAns).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExtraquestionAns', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExtraquestionAns', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExtraquestionAns', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExtraquestionAnsToCollectionIfMissing', () => {
      it('should add a ExtraquestionAns to an empty array', () => {
        const extraquestionAns: IExtraquestionAns = sampleWithRequiredData;
        expectedResult = service.addExtraquestionAnsToCollectionIfMissing([], extraquestionAns);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extraquestionAns);
      });

      it('should not add a ExtraquestionAns to an array that contains it', () => {
        const extraquestionAns: IExtraquestionAns = sampleWithRequiredData;
        const extraquestionAnsCollection: IExtraquestionAns[] = [
          {
            ...extraquestionAns,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExtraquestionAnsToCollectionIfMissing(extraquestionAnsCollection, extraquestionAns);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExtraquestionAns to an array that doesn't contain it", () => {
        const extraquestionAns: IExtraquestionAns = sampleWithRequiredData;
        const extraquestionAnsCollection: IExtraquestionAns[] = [sampleWithPartialData];
        expectedResult = service.addExtraquestionAnsToCollectionIfMissing(extraquestionAnsCollection, extraquestionAns);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extraquestionAns);
      });

      it('should add only unique ExtraquestionAns to an array', () => {
        const extraquestionAnsArray: IExtraquestionAns[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const extraquestionAnsCollection: IExtraquestionAns[] = [sampleWithRequiredData];
        expectedResult = service.addExtraquestionAnsToCollectionIfMissing(extraquestionAnsCollection, ...extraquestionAnsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const extraquestionAns: IExtraquestionAns = sampleWithRequiredData;
        const extraquestionAns2: IExtraquestionAns = sampleWithPartialData;
        expectedResult = service.addExtraquestionAnsToCollectionIfMissing([], extraquestionAns, extraquestionAns2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extraquestionAns);
        expect(expectedResult).toContain(extraquestionAns2);
      });

      it('should accept null and undefined values', () => {
        const extraquestionAns: IExtraquestionAns = sampleWithRequiredData;
        expectedResult = service.addExtraquestionAnsToCollectionIfMissing([], null, extraquestionAns, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extraquestionAns);
      });

      it('should return initial array if no ExtraquestionAns is added', () => {
        const extraquestionAnsCollection: IExtraquestionAns[] = [sampleWithRequiredData];
        expectedResult = service.addExtraquestionAnsToCollectionIfMissing(extraquestionAnsCollection, undefined, null);
        expect(expectedResult).toEqual(extraquestionAnsCollection);
      });
    });

    describe('compareExtraquestionAns', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExtraquestionAns(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExtraquestionAns(entity1, entity2);
        const compareResult2 = service.compareExtraquestionAns(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExtraquestionAns(entity1, entity2);
        const compareResult2 = service.compareExtraquestionAns(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExtraquestionAns(entity1, entity2);
        const compareResult2 = service.compareExtraquestionAns(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
