import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProductFormService } from './product-form.service';
import { ProductService } from '../service/product.service';
import { IProduct } from '../product.model';
import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IIngridient } from 'app/entities/ingridient/ingridient.model';
import { IngridientService } from 'app/entities/ingridient/service/ingridient.service';

import { ProductUpdateComponent } from './product-update.component';

describe('Product Management Update Component', () => {
  let comp: ProductUpdateComponent;
  let fixture: ComponentFixture<ProductUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productFormService: ProductFormService;
  let productService: ProductService;
  let stockService: StockService;
  let categoryService: CategoryService;
  let ingridientService: IngridientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProductUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productFormService = TestBed.inject(ProductFormService);
    productService = TestBed.inject(ProductService);
    stockService = TestBed.inject(StockService);
    categoryService = TestBed.inject(CategoryService);
    ingridientService = TestBed.inject(IngridientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Stock query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const stock: IStock = { id: 21927 };
      product.stock = stock;

      const stockCollection: IStock[] = [{ id: 54620 }];
      jest.spyOn(stockService, 'query').mockReturnValue(of(new HttpResponse({ body: stockCollection })));
      const additionalStocks = [stock];
      const expectedCollection: IStock[] = [...additionalStocks, ...stockCollection];
      jest.spyOn(stockService, 'addStockToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(stockService.query).toHaveBeenCalled();
      expect(stockService.addStockToCollectionIfMissing).toHaveBeenCalledWith(
        stockCollection,
        ...additionalStocks.map(expect.objectContaining)
      );
      expect(comp.stocksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Category query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const category: ICategory = { id: 52954 };
      product.category = category;

      const categoryCollection: ICategory[] = [{ id: 63621 }];
      jest.spyOn(categoryService, 'query').mockReturnValue(of(new HttpResponse({ body: categoryCollection })));
      const additionalCategories = [category];
      const expectedCollection: ICategory[] = [...additionalCategories, ...categoryCollection];
      jest.spyOn(categoryService, 'addCategoryToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(categoryService.query).toHaveBeenCalled();
      expect(categoryService.addCategoryToCollectionIfMissing).toHaveBeenCalledWith(
        categoryCollection,
        ...additionalCategories.map(expect.objectContaining)
      );
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ingridient query and add missing value', () => {
      const product: IProduct = { id: 456 };
      const ingridient: IIngridient = { id: 53543 };
      product.ingridient = ingridient;

      const ingridientCollection: IIngridient[] = [{ id: 70206 }];
      jest.spyOn(ingridientService, 'query').mockReturnValue(of(new HttpResponse({ body: ingridientCollection })));
      const additionalIngridients = [ingridient];
      const expectedCollection: IIngridient[] = [...additionalIngridients, ...ingridientCollection];
      jest.spyOn(ingridientService, 'addIngridientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(ingridientService.query).toHaveBeenCalled();
      expect(ingridientService.addIngridientToCollectionIfMissing).toHaveBeenCalledWith(
        ingridientCollection,
        ...additionalIngridients.map(expect.objectContaining)
      );
      expect(comp.ingridientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const product: IProduct = { id: 456 };
      const stock: IStock = { id: 45730 };
      product.stock = stock;
      const category: ICategory = { id: 32126 };
      product.category = category;
      const ingridient: IIngridient = { id: 86180 };
      product.ingridient = ingridient;

      activatedRoute.data = of({ product });
      comp.ngOnInit();

      expect(comp.stocksSharedCollection).toContain(stock);
      expect(comp.categoriesSharedCollection).toContain(category);
      expect(comp.ingridientsSharedCollection).toContain(ingridient);
      expect(comp.product).toEqual(product);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue(product);
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productService.update).toHaveBeenCalledWith(expect.objectContaining(product));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productFormService, 'getProduct').mockReturnValue({ id: null });
      jest.spyOn(productService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: product }));
      saveSubject.complete();

      // THEN
      expect(productFormService.getProduct).toHaveBeenCalled();
      expect(productService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProduct>>();
      const product = { id: 123 };
      jest.spyOn(productService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ product });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStock', () => {
      it('Should forward to stockService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(stockService, 'compareStock');
        comp.compareStock(entity, entity2);
        expect(stockService.compareStock).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCategory', () => {
      it('Should forward to categoryService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(categoryService, 'compareCategory');
        comp.compareCategory(entity, entity2);
        expect(categoryService.compareCategory).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareIngridient', () => {
      it('Should forward to ingridientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ingridientService, 'compareIngridient');
        comp.compareIngridient(entity, entity2);
        expect(ingridientService.compareIngridient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
