import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProposal, NewProposal } from '../proposal.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProposal for edit and NewProposalFormGroupInput for create.
 */
type ProposalFormGroupInput = IProposal | PartialWithRequiredKeyOf<NewProposal>;

type ProposalFormDefaults = Pick<NewProposal, 'id'>;

type ProposalFormGroupContent = {
  id: FormControl<IProposal['id'] | NewProposal['id']>;
  title: FormControl<IProposal['title']>;
  archive: FormControl<IProposal['archive']>;
  state: FormControl<IProposal['state']>;
  comments: FormControl<IProposal['comments']>;
  student: FormControl<IProposal['student']>;
};

export type ProposalFormGroup = FormGroup<ProposalFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProposalFormService {
  createProposalFormGroup(proposal: ProposalFormGroupInput = { id: null }): ProposalFormGroup {
    const proposalRawValue = {
      ...this.getFormDefaults(),
      ...proposal,
    };
    return new FormGroup<ProposalFormGroupContent>({
      id: new FormControl(
        { value: proposalRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(proposalRawValue.title),
      archive: new FormControl(proposalRawValue.archive),
      state: new FormControl(proposalRawValue.state),
      comments: new FormControl(proposalRawValue.comments),
      student: new FormControl(proposalRawValue.student),
    });
  }

  getProposal(form: ProposalFormGroup): IProposal | NewProposal {
    return form.getRawValue() as IProposal | NewProposal;
  }

  resetForm(form: ProposalFormGroup, proposal: ProposalFormGroupInput): void {
    const proposalRawValue = { ...this.getFormDefaults(), ...proposal };
    form.reset(
      {
        ...proposalRawValue,
        id: { value: proposalRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProposalFormDefaults {
    return {
      id: null,
    };
  }
}
