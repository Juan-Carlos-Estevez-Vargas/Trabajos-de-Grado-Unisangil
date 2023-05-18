import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITutor, NewTutor } from '../tutor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITutor for edit and NewTutorFormGroupInput for create.
 */
type TutorFormGroupInput = ITutor | PartialWithRequiredKeyOf<NewTutor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITutor | NewTutor> = Omit<T, 'hireDate'> & {
  hireDate?: string | null;
};

type TutorFormRawValue = FormValueOf<ITutor>;

type NewTutorFormRawValue = FormValueOf<NewTutor>;

type TutorFormDefaults = Pick<NewTutor, 'id' | 'hireDate'>;

type TutorFormGroupContent = {
  id: FormControl<TutorFormRawValue['id'] | NewTutor['id']>;
  firstName: FormControl<TutorFormRawValue['firstName']>;
  lastName: FormControl<TutorFormRawValue['lastName']>;
  email: FormControl<TutorFormRawValue['email']>;
  phoneNumber: FormControl<TutorFormRawValue['phoneNumber']>;
  hireDate: FormControl<TutorFormRawValue['hireDate']>;
};

export type TutorFormGroup = FormGroup<TutorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TutorFormService {
  createTutorFormGroup(tutor: TutorFormGroupInput = { id: null }): TutorFormGroup {
    const tutorRawValue = this.convertTutorToTutorRawValue({
      ...this.getFormDefaults(),
      ...tutor,
    });
    return new FormGroup<TutorFormGroupContent>({
      id: new FormControl(
        { value: tutorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      firstName: new FormControl(tutorRawValue.firstName),
      lastName: new FormControl(tutorRawValue.lastName),
      email: new FormControl(tutorRawValue.email),
      phoneNumber: new FormControl(tutorRawValue.phoneNumber),
      hireDate: new FormControl(tutorRawValue.hireDate),
    });
  }

  getTutor(form: TutorFormGroup): ITutor | NewTutor {
    return this.convertTutorRawValueToTutor(form.getRawValue() as TutorFormRawValue | NewTutorFormRawValue);
  }

  resetForm(form: TutorFormGroup, tutor: TutorFormGroupInput): void {
    const tutorRawValue = this.convertTutorToTutorRawValue({ ...this.getFormDefaults(), ...tutor });
    form.reset(
      {
        ...tutorRawValue,
        id: { value: tutorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TutorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      hireDate: currentTime,
    };
  }

  private convertTutorRawValueToTutor(rawTutor: TutorFormRawValue | NewTutorFormRawValue): ITutor | NewTutor {
    return {
      ...rawTutor,
      hireDate: dayjs(rawTutor.hireDate, DATE_TIME_FORMAT),
    };
  }

  private convertTutorToTutorRawValue(
    tutor: ITutor | (Partial<NewTutor> & TutorFormDefaults)
  ): TutorFormRawValue | PartialWithRequiredKeyOf<NewTutorFormRawValue> {
    return {
      ...tutor,
      hireDate: tutor.hireDate ? tutor.hireDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
