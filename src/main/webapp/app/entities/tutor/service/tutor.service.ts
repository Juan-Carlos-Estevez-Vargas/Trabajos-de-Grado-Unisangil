import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITutor, NewTutor } from '../tutor.model';

export type PartialUpdateTutor = Partial<ITutor> & Pick<ITutor, 'id'>;

type RestOf<T extends ITutor | NewTutor> = Omit<T, 'hireDate'> & {
  hireDate?: string | null;
};

export type RestTutor = RestOf<ITutor>;

export type NewRestTutor = RestOf<NewTutor>;

export type PartialUpdateRestTutor = RestOf<PartialUpdateTutor>;

export type EntityResponseType = HttpResponse<ITutor>;
export type EntityArrayResponseType = HttpResponse<ITutor[]>;

@Injectable({ providedIn: 'root' })
export class TutorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tutors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tutor: NewTutor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tutor);
    return this.http.post<RestTutor>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(tutor: ITutor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tutor);
    return this.http
      .put<RestTutor>(`${this.resourceUrl}/${this.getTutorIdentifier(tutor)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(tutor: PartialUpdateTutor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(tutor);
    return this.http
      .patch<RestTutor>(`${this.resourceUrl}/${this.getTutorIdentifier(tutor)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTutor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTutor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTutorIdentifier(tutor: Pick<ITutor, 'id'>): number {
    return tutor.id;
  }

  compareTutor(o1: Pick<ITutor, 'id'> | null, o2: Pick<ITutor, 'id'> | null): boolean {
    return o1 && o2 ? this.getTutorIdentifier(o1) === this.getTutorIdentifier(o2) : o1 === o2;
  }

  addTutorToCollectionIfMissing<Type extends Pick<ITutor, 'id'>>(
    tutorCollection: Type[],
    ...tutorsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tutors: Type[] = tutorsToCheck.filter(isPresent);
    if (tutors.length > 0) {
      const tutorCollectionIdentifiers = tutorCollection.map(tutorItem => this.getTutorIdentifier(tutorItem)!);
      const tutorsToAdd = tutors.filter(tutorItem => {
        const tutorIdentifier = this.getTutorIdentifier(tutorItem);
        if (tutorCollectionIdentifiers.includes(tutorIdentifier)) {
          return false;
        }
        tutorCollectionIdentifiers.push(tutorIdentifier);
        return true;
      });
      return [...tutorsToAdd, ...tutorCollection];
    }
    return tutorCollection;
  }

  protected convertDateFromClient<T extends ITutor | NewTutor | PartialUpdateTutor>(tutor: T): RestOf<T> {
    return {
      ...tutor,
      hireDate: tutor.hireDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restTutor: RestTutor): ITutor {
    return {
      ...restTutor,
      hireDate: restTutor.hireDate ? dayjs(restTutor.hireDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTutor>): HttpResponse<ITutor> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTutor[]>): HttpResponse<ITutor[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
