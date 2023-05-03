import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { TutorFormService, TutorFormGroup } from './tutor-form.service';
import { ITutor } from '../tutor.model';
import { TutorService } from '../service/tutor.service';

@Component({
  selector: 'jhi-tutor-update',
  templateUrl: './tutor-update.component.html',
})
export class TutorUpdateComponent implements OnInit {
  isSaving = false;
  tutor: ITutor | null = null;

  editForm: TutorFormGroup = this.tutorFormService.createTutorFormGroup();

  constructor(
    protected tutorService: TutorService,
    protected tutorFormService: TutorFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tutor }) => {
      this.tutor = tutor;
      if (tutor) {
        this.updateForm(tutor);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tutor = this.tutorFormService.getTutor(this.editForm);
    if (tutor.id !== null) {
      this.subscribeToSaveResponse(this.tutorService.update(tutor));
    } else {
      this.subscribeToSaveResponse(this.tutorService.create(tutor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITutor>>): void {
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

  protected updateForm(tutor: ITutor): void {
    this.tutor = tutor;
    this.tutorFormService.resetForm(this.editForm, tutor);
  }
}
