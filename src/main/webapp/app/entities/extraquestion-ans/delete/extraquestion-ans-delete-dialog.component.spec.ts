jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { provideHttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ExtraquestionAnsService } from '../service/extraquestion-ans.service';

import { ExtraquestionAnsDeleteDialogComponent } from './extraquestion-ans-delete-dialog.component';

describe('ExtraquestionAns Management Delete Component', () => {
  let comp: ExtraquestionAnsDeleteDialogComponent;
  let fixture: ComponentFixture<ExtraquestionAnsDeleteDialogComponent>;
  let service: ExtraquestionAnsService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExtraquestionAnsDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(ExtraquestionAnsDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ExtraquestionAnsDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ExtraquestionAnsService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
