import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IngridientComponent } from './list/ingridient.component';
import { IngridientDetailComponent } from './detail/ingridient-detail.component';
import { IngridientUpdateComponent } from './update/ingridient-update.component';
import { IngridientDeleteDialogComponent } from './delete/ingridient-delete-dialog.component';
import { IngridientRoutingModule } from './route/ingridient-routing.module';

@NgModule({
  imports: [SharedModule, IngridientRoutingModule],
  declarations: [IngridientComponent, IngridientDetailComponent, IngridientUpdateComponent, IngridientDeleteDialogComponent],
})
export class IngridientModule {}
