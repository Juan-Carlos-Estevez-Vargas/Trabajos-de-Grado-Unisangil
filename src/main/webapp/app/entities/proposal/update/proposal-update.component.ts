import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ProposalFormService, ProposalFormGroup } from './proposal-form.service';
import { IProposal } from '../proposal.model';
import { ProposalService } from '../service/proposal.service';
import { State } from 'app/entities/enumerations/state.model';

@Component({
  selector: 'jhi-proposal-update',
  templateUrl: './proposal-update.component.html',
})
export class ProposalUpdateComponent implements OnInit {
  isSaving = false;
  proposal: IProposal | null = null;
  stateValues = Object.keys(State);

  editForm: ProposalFormGroup = this.proposalFormService.createProposalFormGroup();

  constructor(
    protected proposalService: ProposalService,
    protected proposalFormService: ProposalFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposal }) => {
      this.proposal = proposal;
      if (proposal) {
        this.updateForm(proposal);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proposal = this.proposalFormService.getProposal(this.editForm);
    if (proposal.id !== null) {
      this.subscribeToSaveResponse(this.proposalService.update(proposal));
    } else {
      this.subscribeToSaveResponse(this.proposalService.create(proposal));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposal>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(proposal: IProposal): void {
    this.proposal = proposal;
    this.proposalFormService.resetForm(this.editForm, proposal);
  }
}
