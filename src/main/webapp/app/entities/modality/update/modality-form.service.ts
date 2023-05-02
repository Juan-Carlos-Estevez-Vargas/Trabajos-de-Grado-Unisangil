import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IModality, NewModality } from '../modality.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IModality for edit and NewModalityFormGroupInput for create.
 */
type ModalityFormGroupInput = IModality | PartialWithRequiredKeyOf<NewModality>;

type ModalityFormDefaults = Pick<NewModality, 'id'>;

type ModalityFormGroupContent = {
  id: FormControl<IModality['id'] | NewModality['id']>;
  name: FormControl<IModality['name']>;
  description: FormControl<IModality['description']>;
  document: FormControl<IModality['document']>;
};

export type ModalityFormGroup = FormGroup<ModalityFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ModalityFormService {
  createModalityFormGroup(modality: ModalityFormGroupInput = { id: null }): ModalityFormGroup {
    const modalityRawValue = {
      ...this.getFormDefaults(),
      ...modality,
    };
    return new FormGroup<ModalityFormGroupContent>({
      id: new FormControl(
        { value: modalityRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(modalityRawValue.name),
      description: new FormControl(modalityRawValue.description),
      document: new FormControl(modalityRawValue.document),
    });
  }

  getModality(form: ModalityFormGroup): IModality | NewModality {
    return form.getRawValue() as IModality | NewModality;
  }

  resetForm(form: ModalityFormGroup, modality: ModalityFormGroupInput): void {
    const modalityRawValue = { ...this.getFormDefaults(), ...modality };
    form.reset(
      {
        ...modalityRawValue,
        id: { value: modalityRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ModalityFormDefaults {
    return {
      id: null,
    };
  }
}
