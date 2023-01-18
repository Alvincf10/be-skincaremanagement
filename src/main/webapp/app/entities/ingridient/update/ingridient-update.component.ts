import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IngridientFormService, IngridientFormGroup } from './ingridient-form.service';
import { IIngridient } from '../ingridient.model';
import { IngridientService } from '../service/ingridient.service';

@Component({
  selector: 'jhi-ingridient-update',
  templateUrl: './ingridient-update.component.html',
})
export class IngridientUpdateComponent implements OnInit {
  isSaving = false;
  ingridient: IIngridient | null = null;

  editForm: IngridientFormGroup = this.ingridientFormService.createIngridientFormGroup();

  constructor(
    protected ingridientService: IngridientService,
    protected ingridientFormService: IngridientFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingridient }) => {
      this.ingridient = ingridient;
      if (ingridient) {
        this.updateForm(ingridient);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ingridient = this.ingridientFormService.getIngridient(this.editForm);
    if (ingridient.id !== null) {
      this.subscribeToSaveResponse(this.ingridientService.update(ingridient));
    } else {
      this.subscribeToSaveResponse(this.ingridientService.create(ingridient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIngridient>>): void {
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

  protected updateForm(ingridient: IIngridient): void {
    this.ingridient = ingridient;
    this.ingridientFormService.resetForm(this.editForm, ingridient);
  }
}
