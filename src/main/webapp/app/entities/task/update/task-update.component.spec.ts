import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TaskFormService } from './task-form.service';
import { TaskService } from '../service/task.service';
import { ITask } from '../task.model';
import { IToDoList } from 'app/entities/to-do-list/to-do-list.model';
import { ToDoListService } from 'app/entities/to-do-list/service/to-do-list.service';

import { TaskUpdateComponent } from './task-update.component';

describe('Task Management Update Component', () => {
  let comp: TaskUpdateComponent;
  let fixture: ComponentFixture<TaskUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let taskFormService: TaskFormService;
  let taskService: TaskService;
  let toDoListService: ToDoListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TaskUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TaskUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TaskUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    taskFormService = TestBed.inject(TaskFormService);
    taskService = TestBed.inject(TaskService);
    toDoListService = TestBed.inject(ToDoListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ToDoList query and add missing value', () => {
      const task: ITask = { id: 'CBA' };
      const toDoList: IToDoList = { id: '7d9f6fbb-6c68-449b-b60b-20003ac3d219' };
      task.toDoList = toDoList;

      const toDoListCollection: IToDoList[] = [{ id: 'a8f9576b-585f-4eb7-8225-f35cb5f2a0e7' }];
      jest.spyOn(toDoListService, 'query').mockReturnValue(of(new HttpResponse({ body: toDoListCollection })));
      const additionalToDoLists = [toDoList];
      const expectedCollection: IToDoList[] = [...additionalToDoLists, ...toDoListCollection];
      jest.spyOn(toDoListService, 'addToDoListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(toDoListService.query).toHaveBeenCalled();
      expect(toDoListService.addToDoListToCollectionIfMissing).toHaveBeenCalledWith(
        toDoListCollection,
        ...additionalToDoLists.map(expect.objectContaining)
      );
      expect(comp.toDoListsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const task: ITask = { id: 'CBA' };
      const toDoList: IToDoList = { id: '35e2974d-85b0-43d0-ab24-a021e2d9341d' };
      task.toDoList = toDoList;

      activatedRoute.data = of({ task });
      comp.ngOnInit();

      expect(comp.toDoListsSharedCollection).toContain(toDoList);
      expect(comp.task).toEqual(task);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 'ABC' };
      jest.spyOn(taskFormService, 'getTask').mockReturnValue(task);
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(taskFormService.getTask).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(taskService.update).toHaveBeenCalledWith(expect.objectContaining(task));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 'ABC' };
      jest.spyOn(taskFormService, 'getTask').mockReturnValue({ id: null });
      jest.spyOn(taskService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: task }));
      saveSubject.complete();

      // THEN
      expect(taskFormService.getTask).toHaveBeenCalled();
      expect(taskService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITask>>();
      const task = { id: 'ABC' };
      jest.spyOn(taskService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ task });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(taskService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareToDoList', () => {
      it('Should forward to toDoListService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(toDoListService, 'compareToDoList');
        comp.compareToDoList(entity, entity2);
        expect(toDoListService.compareToDoList).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
