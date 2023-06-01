import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ToDoListFormService } from './to-do-list-form.service';
import { ToDoListService } from '../service/to-do-list.service';
import { IToDoList } from '../to-do-list.model';
import { IProfil } from 'app/entities/profil/profil.model';
import { ProfilService } from 'app/entities/profil/service/profil.service';

import { ToDoListUpdateComponent } from './to-do-list-update.component';

describe('ToDoList Management Update Component', () => {
  let comp: ToDoListUpdateComponent;
  let fixture: ComponentFixture<ToDoListUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let toDoListFormService: ToDoListFormService;
  let toDoListService: ToDoListService;
  let profilService: ProfilService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ToDoListUpdateComponent],
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
      .overrideTemplate(ToDoListUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ToDoListUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    toDoListFormService = TestBed.inject(ToDoListFormService);
    toDoListService = TestBed.inject(ToDoListService);
    profilService = TestBed.inject(ProfilService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Profil query and add missing value', () => {
      const toDoList: IToDoList = { id: 'CBA' };
      const affectedTos: IProfil[] = [{ id: '757d4982-f044-4a9f-a7ec-e00bb838e346' }];
      toDoList.affectedTos = affectedTos;
      const owner: IProfil = { id: '67f8e16f-e723-427e-9cc9-316092668360' };
      toDoList.owner = owner;

      const profilCollection: IProfil[] = [{ id: 'ee3ab7d5-7752-4687-8e8f-b1979a34e4f4' }];
      jest.spyOn(profilService, 'query').mockReturnValue(of(new HttpResponse({ body: profilCollection })));
      const additionalProfils = [...affectedTos, owner];
      const expectedCollection: IProfil[] = [...additionalProfils, ...profilCollection];
      jest.spyOn(profilService, 'addProfilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ toDoList });
      comp.ngOnInit();

      expect(profilService.query).toHaveBeenCalled();
      expect(profilService.addProfilToCollectionIfMissing).toHaveBeenCalledWith(
        profilCollection,
        ...additionalProfils.map(expect.objectContaining)
      );
      expect(comp.profilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const toDoList: IToDoList = { id: 'CBA' };
      const affectedTo: IProfil = { id: 'db13c966-f486-42da-9c74-4ad6a905f742' };
      toDoList.affectedTos = [affectedTo];
      const owner: IProfil = { id: '5938bc8b-63a7-45ec-9060-f3d64a76ee82' };
      toDoList.owner = owner;

      activatedRoute.data = of({ toDoList });
      comp.ngOnInit();

      expect(comp.profilsSharedCollection).toContain(affectedTo);
      expect(comp.profilsSharedCollection).toContain(owner);
      expect(comp.toDoList).toEqual(toDoList);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IToDoList>>();
      const toDoList = { id: 'ABC' };
      jest.spyOn(toDoListFormService, 'getToDoList').mockReturnValue(toDoList);
      jest.spyOn(toDoListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ toDoList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: toDoList }));
      saveSubject.complete();

      // THEN
      expect(toDoListFormService.getToDoList).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(toDoListService.update).toHaveBeenCalledWith(expect.objectContaining(toDoList));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IToDoList>>();
      const toDoList = { id: 'ABC' };
      jest.spyOn(toDoListFormService, 'getToDoList').mockReturnValue({ id: null });
      jest.spyOn(toDoListService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ toDoList: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: toDoList }));
      saveSubject.complete();

      // THEN
      expect(toDoListFormService.getToDoList).toHaveBeenCalled();
      expect(toDoListService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IToDoList>>();
      const toDoList = { id: 'ABC' };
      jest.spyOn(toDoListService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ toDoList });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(toDoListService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProfil', () => {
      it('Should forward to profilService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(profilService, 'compareProfil');
        comp.compareProfil(entity, entity2);
        expect(profilService.compareProfil).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
