import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIngridient } from '../ingridient.model';
import { IngridientService } from '../service/ingridient.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './ingridient-delete-dialog.component.html',
})
export class IngridientDeleteDialogComponent {
  ingridient?: IIngridient;

  constructor(protected ingridientService: IngridientService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ingridientService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
