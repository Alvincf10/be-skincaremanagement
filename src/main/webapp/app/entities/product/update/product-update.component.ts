import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ProductFormService, ProductFormGroup } from './product-form.service';
import { IProduct } from '../product.model';
import { ProductService } from '../service/product.service';
import { IStock } from 'app/entities/stock/stock.model';
import { StockService } from 'app/entities/stock/service/stock.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IIngridient } from 'app/entities/ingridient/ingridient.model';
import { IngridientService } from 'app/entities/ingridient/service/ingridient.service';

@Component({
  selector: 'jhi-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;
  product: IProduct | null = null;

  stocksSharedCollection: IStock[] = [];
  categoriesSharedCollection: ICategory[] = [];
  ingridientsSharedCollection: IIngridient[] = [];

  editForm: ProductFormGroup = this.productFormService.createProductFormGroup();

  constructor(
    protected productService: ProductService,
    protected productFormService: ProductFormService,
    protected stockService: StockService,
    protected categoryService: CategoryService,
    protected ingridientService: IngridientService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareStock = (o1: IStock | null, o2: IStock | null): boolean => this.stockService.compareStock(o1, o2);

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  compareIngridient = (o1: IIngridient | null, o2: IIngridient | null): boolean => this.ingridientService.compareIngridient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.product = product;
      if (product) {
        this.updateForm(product);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.productFormService.getProduct(this.editForm);
    if (product.id !== null) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(product: IProduct): void {
    this.product = product;
    this.productFormService.resetForm(this.editForm, product);

    this.stocksSharedCollection = this.stockService.addStockToCollectionIfMissing<IStock>(this.stocksSharedCollection, product.stock);
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      product.category
    );
    this.ingridientsSharedCollection = this.ingridientService.addIngridientToCollectionIfMissing<IIngridient>(
      this.ingridientsSharedCollection,
      product.ingridient
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stockService
      .query()
      .pipe(map((res: HttpResponse<IStock[]>) => res.body ?? []))
      .pipe(map((stocks: IStock[]) => this.stockService.addStockToCollectionIfMissing<IStock>(stocks, this.product?.stock)))
      .subscribe((stocks: IStock[]) => (this.stocksSharedCollection = stocks));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, this.product?.category)
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));

    this.ingridientService
      .query()
      .pipe(map((res: HttpResponse<IIngridient[]>) => res.body ?? []))
      .pipe(
        map((ingridients: IIngridient[]) =>
          this.ingridientService.addIngridientToCollectionIfMissing<IIngridient>(ingridients, this.product?.ingridient)
        )
      )
      .subscribe((ingridients: IIngridient[]) => (this.ingridientsSharedCollection = ingridients));
  }
}
