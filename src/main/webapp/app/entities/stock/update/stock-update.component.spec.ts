import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StockFormService } from './stock-form.service';
import { StockService } from '../service/stock.service';
import { IStock } from '../stock.model';

import { StockUpdateComponent } from './stock-update.component';

describe('Stock Management Update Component', () => {
  let comp: StockUpdateComponent;
  let fixture: ComponentFixture<StockUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stockFormService: StockFormService;
  let stockService: StockService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StockUpdateComponent],
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
      .overrideTemplate(StockUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StockUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stockFormService = TestBed.inject(StockFormService);
    stockService = TestBed.inject(StockService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stock: IStock = { id: 456 };

      activatedRoute.data = of({ stock });
      comp.ngOnInit();

      expect(comp.stock).toEqual(stock);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStock>>();
      const stock = { id: 123 };
      jest.spyOn(stockFormService, 'getStock').mockReturnValue(stock);
      jest.spyOn(stockService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stock }));
      saveSubject.complete();

      // THEN
      expect(stockFormService.getStock).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stockService.update).toHaveBeenCalledWith(expect.objectContaining(stock));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStock>>();
      const stock = { id: 123 };
      jest.spyOn(stockFormService, 'getStock').mockReturnValue({ id: null });
      jest.spyOn(stockService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stock: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stock }));
      saveSubject.complete();

      // THEN
      expect(stockFormService.getStock).toHaveBeenCalled();
      expect(stockService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStock>>();
      const stock = { id: 123 };
      jest.spyOn(stockService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stock });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stockService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
