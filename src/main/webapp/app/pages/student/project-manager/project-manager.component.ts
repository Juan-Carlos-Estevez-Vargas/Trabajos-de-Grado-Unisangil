import { Component, OnInit } from '@angular/core';
import { TutorService } from 'app/entities/tutor/service/tutor.service';
import { ITutor } from 'app/entities/tutor/tutor.model';

@Component({
  selector: 'jhi-project-manager',
  templateUrl: './project-manager.component.html',
  styleUrls: ['./project-manager.component.scss'],
})
export class ProjectManagerComponent implements OnInit {
  managers: ITutor[] = [];

  constructor(private tutorService: TutorService) {}

  ngOnInit(): void {
    this.getTutors();
  }

  getTutors(): void {
    this.tutorService.query().subscribe(response => {
      this.managers = response.body ?? [];
    });
  }
}
