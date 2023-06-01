import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IToDoList } from '../to-do-list.model';

@Component({
  selector: 'jhi-to-do-list-detail',
  templateUrl: './to-do-list-detail.component.html',
})
export class ToDoListDetailComponent implements OnInit {
  toDoList: IToDoList | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ toDoList }) => {
      this.toDoList = toDoList;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
