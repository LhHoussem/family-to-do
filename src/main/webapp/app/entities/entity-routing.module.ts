import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'to-do-list',
        data: { pageTitle: 'familyToDoApp.toDoList.home.title' },
        loadChildren: () => import('./to-do-list/to-do-list.module').then(m => m.ToDoListModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'familyToDoApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'profil',
        data: { pageTitle: 'familyToDoApp.profil.home.title' },
        loadChildren: () => import('./profil/profil.module').then(m => m.ProfilModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
