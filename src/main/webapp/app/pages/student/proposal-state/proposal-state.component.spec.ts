import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProposalStateComponent } from './proposal-state.component';

describe('ProposalStateComponent', () => {
  let component: ProposalStateComponent;
  let fixture: ComponentFixture<ProposalStateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProposalStateComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ProposalStateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
