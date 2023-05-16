import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalitiesComponent } from './modalities.component';

describe('ModalitiesComponent', () => {
  let component: ModalitiesComponent;
  let fixture: ComponentFixture<ModalitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalitiesComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
