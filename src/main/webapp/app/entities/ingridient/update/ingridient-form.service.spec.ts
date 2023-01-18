import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ingridient.test-samples';

import { IngridientFormService } from './ingridient-form.service';

describe('Ingridient Form Service', () => {
  let service: IngridientFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(IngridientFormService);
  });

  describe('Service methods', () => {
    describe('createIngridientFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createIngridientFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ingridientName: expect.any(Object),
          })
        );
      });

      it('passing IIngridient should create a new form with FormGroup', () => {
        const formGroup = service.createIngridientFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ingridientName: expect.any(Object),
          })
        );
      });
    });

    describe('getIngridient', () => {
      it('should return NewIngridient for default Ingridient initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createIngridientFormGroup(sampleWithNewData);

        const ingridient = service.getIngridient(formGroup) as any;

        expect(ingridient).toMatchObject(sampleWithNewData);
      });

      it('should return NewIngridient for empty Ingridient initial value', () => {
        const formGroup = service.createIngridientFormGroup();

        const ingridient = service.getIngridient(formGroup) as any;

        expect(ingridient).toMatchObject({});
      });

      it('should return IIngridient', () => {
        const formGroup = service.createIngridientFormGroup(sampleWithRequiredData);

        const ingridient = service.getIngridient(formGroup) as any;

        expect(ingridient).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIngridient should not enable id FormControl', () => {
        const formGroup = service.createIngridientFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIngridient should disable id FormControl', () => {
        const formGroup = service.createIngridientFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
