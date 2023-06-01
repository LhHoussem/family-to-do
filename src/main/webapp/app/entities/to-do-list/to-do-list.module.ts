import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ToDoListComponent } from './list/to-do-list.component';
import { ToDoListDetailComponent } from './detail/to-do-list-detail.component';
import { ToDoListUpdateComponent } from './update/to-do-list-update.component';
import { ToDoListDeleteDialogComponent } from './delete/to-do-list-delete-dialog.component';
import { ToDoListRoutingModule } from './route/to-do-list-routing.module';

@NgModule({
  imports: [SharedModule, ToDoListRoutingModule],
  declarations: [ToDoListComponent, ToDoListDetailComponent, ToDoListUpdateComponent, ToDoListDeleteDialogComponent],
})
export class ToDoListModule {}
