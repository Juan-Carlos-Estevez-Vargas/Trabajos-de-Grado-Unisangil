import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IModality, NewModality } from '../modality.model';

export type PartialUpdateModality = Partial<IModality> & Pick<IModality, 'id'>;

export type EntityResponseType = HttpResponse<IModality>;
export type EntityArrayResponseType = HttpResponse<IModality[]>;

@Injectable({ providedIn: 'root' })
export class ModalityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/modalities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(modality: NewModality): Observable<EntityResponseType> {
    return this.http.post<IModality>(this.resourceUrl, modality, { observe: 'response' });
  }

  update(modality: IModality): Observable<EntityResponseType> {
    return this.http.put<IModality>(`${this.resourceUrl}/${this.getModalityIdentifier(modality)}`, modality, { observe: 'response' });
  }

  partialUpdate(modality: PartialUpdateModality): Observable<EntityResponseType> {
    return this.http.patch<IModality>(`${this.resourceUrl}/${this.getModalityIdentifier(modality)}`, modality, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IModality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IModality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getModalityIdentifier(modality: Pick<IModality, 'id'>): number {
    return modality.id;
  }

  compareModality(o1: Pick<IModality, 'id'> | null, o2: Pick<IModality, 'id'> | null): boolean {
    return o1 && o2 ? this.getModalityIdentifier(o1) === this.getModalityIdentifier(o2) : o1 === o2;
  }

  addModalityToCollectionIfMissing<Type extends Pick<IModality, 'id'>>(
    modalityCollection: Type[],
    ...modalitiesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const modalities: Type[] = modalitiesToCheck.filter(isPresent);
    if (modalities.length > 0) {
      const modalityCollectionIdentifiers = modalityCollection.map(modalityItem => this.getModalityIdentifier(modalityItem)!);
      const modalitiesToAdd = modalities.filter(modalityItem => {
        const modalityIdentifier = this.getModalityIdentifier(modalityItem);
        if (modalityCollectionIdentifiers.includes(modalityIdentifier)) {
          return false;
        }
        modalityCollectionIdentifiers.push(modalityIdentifier);
        return true;
      });
      return [...modalitiesToAdd, ...modalityCollection];
    }
    return modalityCollection;
  }
}
