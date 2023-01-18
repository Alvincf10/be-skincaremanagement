import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IngridientFormService } from './ingridient-form.service';
import { IngridientService } from '../service/ingridient.service';
import { IIngridient } from '../ingridient.model';

import { IngridientUpdateComponent } from './ingridient-update.component';

describe('Ingridient Management Update Component', () => {
  let comp: IngridientUpdateComponent;
  let fixture: ComponentFixture<IngridientUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ingridientFormService: IngridientFormService;
  let ingridientService: IngridientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IngridientUpdateComponent],
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
      .overrideTemplate(IngridientUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IngridientUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ingridientFormService = TestBed.inject(IngridientFormService);
    ingridientService = TestBed.inject(IngridientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ingridient: IIngridient = { id: 456 };

      activatedRoute.data = of({ ingridient });
      comp.ngOnInit();

      expect(comp.ingridient).toEqual(ingridient);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngridient>>();
      const ingridient = { id: 123 };
      jest.spyOn(ingridientFormService, 'getIngridient').mockReturnValue(ingridient);
      jest.spyOn(ingridientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingridient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingridient }));
      saveSubject.complete();

      // THEN
      expect(ingridientFormService.getIngridient).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ingridientService.update).toHaveBeenCalledWith(expect.objectContaining(ingridient));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngridient>>();
      const ingridient = { id: 123 };
      jest.spyOn(ingridientFormService, 'getIngridient').mockReturnValue({ id: null });
      jest.spyOn(ingridientService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingridient: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingridient }));
      saveSubject.complete();

      // THEN
      expect(ingridientFormService.getIngridient).toHaveBeenCalled();
      expect(ingridientService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngridient>>();
      const ingridient = { id: 123 };
      jest.spyOn(ingridientService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingridient });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ingridientService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
