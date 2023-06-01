import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProfilComponent } from '../list/profil.component';
import { ProfilDetailComponent } from '../detail/profil-detail.component';
import { ProfilUpdateComponent } from '../update/profil-update.component';
import { ProfilRoutingResolveService } from './profil-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const profilRoute: Routes = [
  {
    path: '',
    component: ProfilComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProfilDetailComponent,
    resolve: {
      profil: ProfilRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProfilUpdateComponent,
    resolve: {
      profil: ProfilRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProfilUpdateComponent,
    resolve: {
      profil: ProfilRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(profilRoute)],
  exports: [RouterModule],
})
export class ProfilRoutingModule {}
