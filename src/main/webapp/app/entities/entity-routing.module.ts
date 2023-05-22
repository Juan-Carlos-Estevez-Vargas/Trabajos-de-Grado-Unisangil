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
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
