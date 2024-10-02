import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IExtraquestion } from '../extraquestion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../extraquestion.test-samples';

import { ExtraquestionService } from './extraquestion.service';

const requireRestSample: IExtraquestion = {
  ...sampleWithRequiredData,
};

describe('Extraquestion Service', () => {
  let service: ExtraquestionService;
  let httpMock: HttpTestingController;
  let expectedResult: IExtraquestion | IExtraquestion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ExtraquestionService);
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

    it('should create a Extraquestion', () => {
      const extraquestion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(extraquestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Extraquestion', () => {
      const extraquestion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(extraquestion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Extraquestion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Extraquestion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Extraquestion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExtraquestionToCollectionIfMissing', () => {
      it('should add a Extraquestion to an empty array', () => {
        const extraquestion: IExtraquestion = sampleWithRequiredData;
        expectedResult = service.addExtraquestionToCollectionIfMissing([], extraquestion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extraquestion);
      });

      it('should not add a Extraquestion to an array that contains it', () => {
        const extraquestion: IExtraquestion = sampleWithRequiredData;
        const extraquestionCollection: IExtraquestion[] = [
          {
            ...extraquestion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExtraquestionToCollectionIfMissing(extraquestionCollection, extraquestion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Extraquestion to an array that doesn't contain it", () => {
        const extraquestion: IExtraquestion = sampleWithRequiredData;
        const extraquestionCollection: IExtraquestion[] = [sampleWithPartialData];
        expectedResult = service.addExtraquestionToCollectionIfMissing(extraquestionCollection, extraquestion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extraquestion);
      });

      it('should add only unique Extraquestion to an array', () => {
        const extraquestionArray: IExtraquestion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const extraquestionCollection: IExtraquestion[] = [sampleWithRequiredData];
        expectedResult = service.addExtraquestionToCollectionIfMissing(extraquestionCollection, ...extraquestionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const extraquestion: IExtraquestion = sampleWithRequiredData;
        const extraquestion2: IExtraquestion = sampleWithPartialData;
        expectedResult = service.addExtraquestionToCollectionIfMissing([], extraquestion, extraquestion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(extraquestion);
        expect(expectedResult).toContain(extraquestion2);
      });

      it('should accept null and undefined values', () => {
        const extraquestion: IExtraquestion = sampleWithRequiredData;
        expectedResult = service.addExtraquestionToCollectionIfMissing([], null, extraquestion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(extraquestion);
      });

      it('should return initial array if no Extraquestion is added', () => {
        const extraquestionCollection: IExtraquestion[] = [sampleWithRequiredData];
        expectedResult = service.addExtraquestionToCollectionIfMissing(extraquestionCollection, undefined, null);
        expect(expectedResult).toEqual(extraquestionCollection);
      });
    });

    describe('compareExtraquestion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExtraquestion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExtraquestion(entity1, entity2);
        const compareResult2 = service.compareExtraquestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExtraquestion(entity1, entity2);
        const compareResult2 = service.compareExtraquestion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExtraquestion(entity1, entity2);
        const compareResult2 = service.compareExtraquestion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
