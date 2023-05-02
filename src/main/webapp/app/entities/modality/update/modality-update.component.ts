import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ModalityFormService, ModalityFormGroup } from './modality-form.service';
import { IModality } from '../modality.model';
import { ModalityService } from '../service/modality.service';

@Component({
  selector: 'jhi-modality-update',
  templateUrl: './modality-update.component.html',
})
export class ModalityUpdateComponent implements OnInit {
  isSaving = false;
  modality: IModality | null = null;

  editForm: ModalityFormGroup = this.modalityFormService.createModalityFormGroup();

  constructor(
    protected modalityService: ModalityService,
    protected modalityFormService: ModalityFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ modality }) => {
      this.modality = modality;
      if (modality) {
        this.updateForm(modality);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const modality = this.modalityFormService.getModality(this.editForm);
    if (modality.id !== null) {
      this.subscribeToSaveResponse(this.modalityService.update(modality));
    } else {
      this.subscribeToSaveResponse(this.modalityService.create(modality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IModality>>): void {
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

  protected updateForm(modality: IModality): void {
    this.modality = modality;
    this.modalityFormService.resetForm(this.editForm, modality);
  }
}
