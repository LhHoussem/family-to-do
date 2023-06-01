import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProfilComponent } from './list/profil.component';
import { ProfilDetailComponent } from './detail/profil-detail.component';
import { ProfilUpdateComponent } from './update/profil-update.component';
import { ProfilDeleteDialogComponent } from './delete/profil-delete-dialog.component';
import { ProfilRoutingModule } from './route/profil-routing.module';

@NgModule({
  imports: [SharedModule, ProfilRoutingModule],
  declarations: [ProfilComponent, ProfilDetailComponent, ProfilUpdateComponent, ProfilDeleteDialogComponent],
})
export class ProfilModule {}
