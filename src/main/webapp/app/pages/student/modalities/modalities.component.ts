import { Component, OnInit } from '@angular/core';
import { IModality } from 'app/entities/modality/modality.model';
import { ModalityService } from 'app/entities/modality/service/modality.service';

@Component({
  selector: 'jhi-modalities',
  templateUrl: './modalities.component.html',
  styleUrls: ['./modalities.component.scss'],
})
export class ModalitiesComponent implements OnInit {
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
}
