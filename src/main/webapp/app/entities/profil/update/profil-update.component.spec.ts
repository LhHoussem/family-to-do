import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProfilFormService } from './profil-form.service';
import { ProfilService } from '../service/profil.service';
import { IProfil } from '../profil.model';
import { IToDoList } from 'app/entities/to-do-list/to-do-list.model';
import { ToDoListService } from 'app/entities/to-do-list/service/to-do-list.service';

import { ProfilUpdateComponent } from './profil-update.component';

describe('Profil Management Update Component', () => {
  let comp: ProfilUpdateComponent;
  let fixture: ComponentFixture<ProfilUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let profilFormService: ProfilFormService;
  let profilService: ProfilService;
  let toDoListService: ToDoListService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProfilUpdateComponent],
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
      .overrideTemplate(ProfilUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfilUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    profilFormService = TestBed.inject(ProfilFormService);
    profilService = TestBed.inject(ProfilService);
    toDoListService = TestBed.inject(ToDoListService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ToDoList query and add missing value', () => {
      const profil: IProfil = { id: 'CBA' };
      const toDoList: IToDoList = { id: 'f2cdd7a3-ac13-42e9-a9be-352ab8df4a78' };
      profil.toDoList = toDoList;

      const toDoListCollection: IToDoList[] = [{ id: '93d0c74e-8cd6-43d9-b4ae-9853a40d41b6' }];
      jest.spyOn(toDoListService, 'query').mockReturnValue(of(new HttpResponse({ body: toDoListCollection })));
      const additionalToDoLists = [toDoList];
      const expectedCollection: IToDoList[] = [...additionalToDoLists, ...toDoListCollection];
      jest.spyOn(toDoListService, 'addToDoListToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(toDoListService.query).toHaveBeenCalled();
      expect(toDoListService.addToDoListToCollectionIfMissing).toHaveBeenCalledWith(
        toDoListCollection,
        ...additionalToDoLists.map(expect.objectContaining)
      );
      expect(comp.toDoListsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const profil: IProfil = { id: 'CBA' };
      const toDoList: IToDoList = { id: '385e36f0-37ad-411d-ad2b-6de8ed056b75' };
      profil.toDoList = toDoList;

      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      expect(comp.toDoListsSharedCollection).toContain(toDoList);
      expect(comp.profil).toEqual(profil);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 'ABC' };
      jest.spyOn(profilFormService, 'getProfil').mockReturnValue(profil);
      jest.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profil }));
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(profilService.update).toHaveBeenCalledWith(expect.objectContaining(profil));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 'ABC' };
      jest.spyOn(profilFormService, 'getProfil').mockReturnValue({ id: null });
      jest.spyOn(profilService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profil }));
      saveSubject.complete();

      // THEN
      expect(profilFormService.getProfil).toHaveBeenCalled();
      expect(profilService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfil>>();
      const profil = { id: 'ABC' };
      jest.spyOn(profilService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profil });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(profilService.update).toHaveBeenCalled();
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
