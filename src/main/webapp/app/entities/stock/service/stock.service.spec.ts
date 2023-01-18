import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStock } from '../stock.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../stock.test-samples';

import { StockService } from './stock.service';

const requireRestSample: IStock = {
  ...sampleWithRequiredData,
};

describe('Stock Service', () => {
  let service: StockService;
  let httpMock: HttpTestingController;
  let expectedResult: IStock | IStock[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StockService);
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

    it('should create a Stock', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const stock = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stock).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Stock', () => {
      const stock = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stock).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Stock', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Stock', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Stock', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStockToCollectionIfMissing', () => {
      it('should add a Stock to an empty array', () => {
        const stock: IStock = sampleWithRequiredData;
        expectedResult = service.addStockToCollectionIfMissing([], stock);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stock);
      });

      it('should not add a Stock to an array that contains it', () => {
        const stock: IStock = sampleWithRequiredData;
        const stockCollection: IStock[] = [
          {
            ...stock,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStockToCollectionIfMissing(stockCollection, stock);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Stock to an array that doesn't contain it", () => {
        const stock: IStock = sampleWithRequiredData;
        const stockCollection: IStock[] = [sampleWithPartialData];
        expectedResult = service.addStockToCollectionIfMissing(stockCollection, stock);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stock);
      });

      it('should add only unique Stock to an array', () => {
        const stockArray: IStock[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stockCollection: IStock[] = [sampleWithRequiredData];
        expectedResult = service.addStockToCollectionIfMissing(stockCollection, ...stockArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stock: IStock = sampleWithRequiredData;
        const stock2: IStock = sampleWithPartialData;
        expectedResult = service.addStockToCollectionIfMissing([], stock, stock2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stock);
        expect(expectedResult).toContain(stock2);
      });

      it('should accept null and undefined values', () => {
        const stock: IStock = sampleWithRequiredData;
        expectedResult = service.addStockToCollectionIfMissing([], null, stock, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stock);
      });

      it('should return initial array if no Stock is added', () => {
        const stockCollection: IStock[] = [sampleWithRequiredData];
        expectedResult = service.addStockToCollectionIfMissing(stockCollection, undefined, null);
        expect(expectedResult).toEqual(stockCollection);
      });
    });

    describe('compareStock', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStock(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStock(entity1, entity2);
        const compareResult2 = service.compareStock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStock(entity1, entity2);
        const compareResult2 = service.compareStock(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStock(entity1, entity2);
        const compareResult2 = service.compareStock(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
