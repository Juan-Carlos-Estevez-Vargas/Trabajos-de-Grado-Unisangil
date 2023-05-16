import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IModality } from 'app/entities/modality/modality.model';
import { EntityArrayResponseType, ModalityService } from 'app/entities/modality/service/modality.service';

@Component({
  selector: 'jhi-modalities',
  templateUrl: './modalities.component.html',
  styleUrls: ['./modalities.component.scss'],
})
export class ModalitiesComponent implements OnInit {
  // modalities?: IModality[] = [];
  // isLoading = false;

  // predicate = 'id';
  // ascending = true;

  // links: { [key: string]: number } = {
  //   last: 0,
  // };
  // page = 1;

  modalities: IModality[] = [];

  constructor(private modalityService: ModalityService) {}

  ngOnInit(): void {
    this.getModalities();
  }

  getModalities(): void {
    this.modalityService.query().subscribe(response => {
      this.modalities = response.body ?? [];
    });
  }

  // constructor(
  //   protected modalityService: ModalityService,
  //   protected parseLinks: ParseLinks,
  // ) { }

  // ngOnInit(): void {
  //   // this.modalityService.query().subscribe((data) => {
  //   //   this.modalities = data;
  //   // });
  // }

  // protected onResponseSuccess(response: EntityArrayResponseType): void {
  //   this.fillComponentAttributesFromResponseHeader(response.headers);
  //   const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
  //   this.modalities = dataFromBody;
  // }

  // protected fillComponentAttributesFromResponseBody(data: IModality[] | null): IModality[] {
  //   const modalitiesNew = this.modalities ?? [];
  //   if (data) {
  //     for (const d of data) {
  //       if (modalitiesNew.map(op => op.id).indexOf(d.id) === -1) {
  //         modalitiesNew.push(d);
  //       }
  //     }
  //   }
  //   return modalitiesNew;
  // }

  // protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
  //   const linkHeader = headers.get('link');
  //   if (linkHeader) {
  //     this.links = this.parseLinks.parse(linkHeader);
  //   } else {
  //     this.links = {
  //       last: 0,
  //     };
  //   }
  // }
}
