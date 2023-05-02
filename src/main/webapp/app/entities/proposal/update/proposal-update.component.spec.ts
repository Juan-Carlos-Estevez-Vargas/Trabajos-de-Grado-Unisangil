import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProposalFormService } from './proposal-form.service';
import { ProposalService } from '../service/proposal.service';
import { IProposal } from '../proposal.model';

import { ProposalUpdateComponent } from './proposal-update.component';

describe('Proposal Management Update Component', () => {
  let comp: ProposalUpdateComponent;
  let fixture: ComponentFixture<ProposalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let proposalFormService: ProposalFormService;
  let proposalService: ProposalService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProposalUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProposalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProposalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    proposalFormService = TestBed.inject(ProposalFormService);
    proposalService = TestBed.inject(ProposalService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const proposal: IProposal = { id: 456 };

      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      expect(comp.proposal).toEqual(proposal);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProposal>>();
      const proposal = { id: 123 };
      jest.spyOn(proposalFormService, 'getProposal').mockReturnValue(proposal);
      jest.spyOn(proposalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proposal }));
      saveSubject.complete();

      // THEN
      expect(proposalFormService.getProposal).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(proposalService.update).toHaveBeenCalledWith(expect.objectContaining(proposal));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProposal>>();
      const proposal = { id: 123 };
      jest.spyOn(proposalFormService, 'getProposal').mockReturnValue({ id: null });
      jest.spyOn(proposalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposal: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proposal }));
      saveSubject.complete();

      // THEN
      expect(proposalFormService.getProposal).toHaveBeenCalled();
      expect(proposalService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProposal>>();
      const proposal = { id: 123 };
      jest.spyOn(proposalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proposal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(proposalService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
