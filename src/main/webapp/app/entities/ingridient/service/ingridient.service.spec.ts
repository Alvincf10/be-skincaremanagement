import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IIngridient } from '../ingridient.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ingridient.test-samples';

import { IngridientService } from './ingridient.service';

const requireRestSample: IIngridient = {
  ...sampleWithRequiredData,
};

describe('Ingridient Service', () => {
  let service: IngridientService;
  let httpMock: HttpTestingController;
  let expectedResult: IIngridient | IIngridient[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(IngridientService);
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

    it('should create a Ingridient', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ingridient = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ingridient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ingridient', () => {
      const ingridient = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ingridient).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ingridient', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ingridient', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ingridient', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addIngridientToCollectionIfMissing', () => {
      it('should add a Ingridient to an empty array', () => {
        const ingridient: IIngridient = sampleWithRequiredData;
        expectedResult = service.addIngridientToCollectionIfMissing([], ingridient);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingridient);
      });

      it('should not add a Ingridient to an array that contains it', () => {
        const ingridient: IIngridient = sampleWithRequiredData;
        const ingridientCollection: IIngridient[] = [
          {
            ...ingridient,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addIngridientToCollectionIfMissing(ingridientCollection, ingridient);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ingridient to an array that doesn't contain it", () => {
        const ingridient: IIngridient = sampleWithRequiredData;
        const ingridientCollection: IIngridient[] = [sampleWithPartialData];
        expectedResult = service.addIngridientToCollectionIfMissing(ingridientCollection, ingridient);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingridient);
      });

      it('should add only unique Ingridient to an array', () => {
        const ingridientArray: IIngridient[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ingridientCollection: IIngridient[] = [sampleWithRequiredData];
        expectedResult = service.addIngridientToCollectionIfMissing(ingridientCollection, ...ingridientArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ingridient: IIngridient = sampleWithRequiredData;
        const ingridient2: IIngridient = sampleWithPartialData;
        expectedResult = service.addIngridientToCollectionIfMissing([], ingridient, ingridient2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ingridient);
        expect(expectedResult).toContain(ingridient2);
      });

      it('should accept null and undefined values', () => {
        const ingridient: IIngridient = sampleWithRequiredData;
        expectedResult = service.addIngridientToCollectionIfMissing([], null, ingridient, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ingridient);
      });

      it('should return initial array if no Ingridient is added', () => {
        const ingridientCollection: IIngridient[] = [sampleWithRequiredData];
        expectedResult = service.addIngridientToCollectionIfMissing(ingridientCollection, undefined, null);
        expect(expectedResult).toEqual(ingridientCollection);
      });
    });

    describe('compareIngridient', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareIngridient(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareIngridient(entity1, entity2);
        const compareResult2 = service.compareIngridient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareIngridient(entity1, entity2);
        const compareResult2 = service.compareIngridient(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareIngridient(entity1, entity2);
        const compareResult2 = service.compareIngridient(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
