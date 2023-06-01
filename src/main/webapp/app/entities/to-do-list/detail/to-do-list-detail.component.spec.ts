import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ToDoListDetailComponent } from './to-do-list-detail.component';

describe('ToDoList Management Detail Component', () => {
  let comp: ToDoListDetailComponent;
  let fixture: ComponentFixture<ToDoListDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ToDoListDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ toDoList: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ToDoListDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ToDoListDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load toDoList on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.toDoList).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
