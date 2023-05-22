import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { DegreeWorkComponent } from 'app/pages/student/degree-work/degree-work.component';
import { DetailProposalComponent } from 'app/pages/student/detail-proposal/detail-proposal.component';
import { ModalitiesComponent } from 'app/pages/student/modalities/modalities.component';
import { ProjectManagerComponent } from 'app/pages/student/project-manager/project-manager.component';
import { ProposalStateComponent } from 'app/pages/student/proposal-state/proposal-state.component';

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
      { path: 'modalities', component: ModalitiesComponent },
      { path: 'project-manager', component: ProjectManagerComponent },
      { path: 'proposal-state', component: ProposalStateComponent },
      { path: 'proposal-detail', component: DetailProposalComponent },
      { path: 'degree-work', component: DegreeWorkComponent },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
