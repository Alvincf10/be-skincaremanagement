import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IIngridient, NewIngridient } from '../ingridient.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIngridient for edit and NewIngridientFormGroupInput for create.
 */
type IngridientFormGroupInput = IIngridient | PartialWithRequiredKeyOf<NewIngridient>;

type IngridientFormDefaults = Pick<NewIngridient, 'id'>;

type IngridientFormGroupContent = {
  id: FormControl<IIngridient['id'] | NewIngridient['id']>;
  ingridientName: FormControl<IIngridient['ingridientName']>;
};

export type IngridientFormGroup = FormGroup<IngridientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class IngridientFormService {
  createIngridientFormGroup(ingridient: IngridientFormGroupInput = { id: null }): IngridientFormGroup {
    const ingridientRawValue = {
      ...this.getFormDefaults(),
      ...ingridient,
    };
    return new FormGroup<IngridientFormGroupContent>({
      id: new FormControl(
        { value: ingridientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      ingridientName: new FormControl(ingridientRawValue.ingridientName),
    });
  }

  getIngridient(form: IngridientFormGroup): IIngridient | NewIngridient {
    return form.getRawValue() as IIngridient | NewIngridient;
  }

  resetForm(form: IngridientFormGroup, ingridient: IngridientFormGroupInput): void {
    const ingridientRawValue = { ...this.getFormDefaults(), ...ingridient };
    form.reset(
      {
        ...ingridientRawValue,
        id: { value: ingridientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): IngridientFormDefaults {
    return {
      id: null,
    };
  }
}
