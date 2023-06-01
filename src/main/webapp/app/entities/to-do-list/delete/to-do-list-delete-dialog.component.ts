import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IToDoList } from '../to-do-list.model';
import { ToDoListService } from '../service/to-do-list.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './to-do-list-delete-dialog.component.html',
})
export class ToDoListDeleteDialogComponent {
  toDoList?: IToDoList;

  constructor(protected toDoListService: ToDoListService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.toDoListService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
