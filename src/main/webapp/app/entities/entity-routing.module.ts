import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'proposal',
        data: { pageTitle: 'trabajosDeGradoUnisangilApp.proposal.home.title' },
        loadChildren: () => import('./proposal/proposal.module').then(m => m.ProposalModule),
      },
      {
        path: 'modality',
        data: { pageTitle: 'trabajosDeGradoUnisangilApp.modality.home.title' },
        loadChildren: () => import('./modality/modality.module').then(m => m.ModalityModule),
      },
      {
        path: 'tutor',
        data: { pageTitle: 'trabajosDeGradoUnisangilApp.tutor.home.title' },
        loadChildren: () => import('./tutor/tutor.module').then(m => m.TutorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
