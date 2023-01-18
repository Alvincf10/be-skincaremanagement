import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIngridient } from '../ingridient.model';

@Component({
  selector: 'jhi-ingridient-detail',
  templateUrl: './ingridient-detail.component.html',
})
export class IngridientDetailComponent implements OnInit {
  ingridient: IIngridient | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ingridient }) => {
      this.ingridient = ingridient;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
