import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ToDoListComponent } from '../list/to-do-list.component';
import { ToDoListDetailComponent } from '../detail/to-do-list-detail.component';
import { ToDoListUpdateComponent } from '../update/to-do-list-update.component';
import { ToDoListRoutingResolveService } from './to-do-list-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const toDoListRoute: Routes = [
  {
    path: '',
    component: ToDoListComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ToDoListDetailComponent,
    resolve: {
      toDoList: ToDoListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ToDoListUpdateComponent,
    resolve: {
      toDoList: ToDoListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ToDoListUpdateComponent,
    resolve: {
      toDoList: ToDoListRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(toDoListRoute)],
  exports: [RouterModule],
})
export class ToDoListRoutingModule {}
