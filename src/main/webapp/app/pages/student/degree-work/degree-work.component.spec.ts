import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DegreeWorkComponent } from './degree-work.component';

describe('DegreeWorkComponent', () => {
  let component: DegreeWorkComponent;
  let fixture: ComponentFixture<DegreeWorkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DegreeWorkComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DegreeWorkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
