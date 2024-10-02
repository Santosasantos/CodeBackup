import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { Observable, of } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { SelectItem } from 'primeng/api';
import dayjs from 'dayjs';

import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { DropdownModule } from 'primeng/dropdown';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';

import { RatingScaleService } from '../entities/rating-scale/service/rating-scale.service';
import { EmployeeService } from '../entities/employee/service/employee.service';
import { FeedbackFormService } from '../entities/feedback/update/feedback-form.service';
import { FeedbackService } from '../entities/feedback/service/feedback.service';
import { FeedbackResponderFormService } from '../entities/feedback-responder/update/feedback-responder-form.service';
import { FeedbackResponderService } from '../entities/feedback-responder/service/feedback-responder.service';
import { FeedbackRequestService } from './service/feedback-request.service';

import { IEmployee } from '../entities/employee/employee.model';
import { IRatingScale, NewRatingScale } from '../entities/rating-scale/rating-scale.model';
import { IFeedback } from '../entities/feedback/feedback.model';
import { IFeedbackResponder, NewFeedbackResponder } from '../entities/feedback-responder/feedback-responder.model';
import { ResponderCategory } from '../entities/enumerations/responder-category.model';
import { FeedbackStatus } from '../entities/enumerations/feedback-status.model';
import { HttpResponse } from '@angular/common/http';
import { TooltipModule } from 'primeng/tooltip';

@Component({
  selector: 'jhi-feedback-request-form',
  templateUrl: './feedback-request-form.component.html',
  styleUrls: ['./feedback-request-form.component.scss'],
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule,
    CommonModule,
    CardModule,
    DropdownModule,
    AutoCompleteModule,
    ButtonModule,
    TableModule,
    DialogModule,
    InputTextModule,
    TooltipModule,
  ],
})
export class FeedbackRequestFormComponent implements OnInit {
  feedbackForm!: FormGroup;
  existingFeedback: IFeedback | null = null;
  responderCategories: SelectItem[];
  filteredEmployees: any[] = [];
  ratingScales: IRatingScale[] = [];
  selectedResponders: any[] = [];
  isLoading = false;
  requester!: IEmployee;
  assessmentYear: string = `${new Date().getFullYear()}-${new Date().getFullYear() + 1}`;
  currentYear = new Date().getFullYear();
  isAdmin: boolean = true;
  displayCreateRatingDialoge: boolean = false;
  ratingScaleForm!: FormGroup;
  private selfAndSupervisorAdded = false;

  ResponderCategory = ResponderCategory;
  FeedbackStatus = FeedbackStatus;

  constructor(
    private fb: FormBuilder,
    private feedbackRequestService: FeedbackRequestService,
    private feedbackService: FeedbackService,
    private ratingScaleService: RatingScaleService,
    private employeeService: EmployeeService,
    private feedbackFormService: FeedbackFormService,
    private responderFormService: FeedbackResponderFormService,
    private responderService: FeedbackResponderService,
  ) {
    this.responderCategories = Object.values(ResponderCategory).map(category => ({
      label: category,
      value: category,
    }));
  }

  ngOnInit() {
    this.initForm();
    this.initRatingScaleForm();
    this.loadRatingScales();
    this.getTheRequester();
  }

  initForm() {
    this.feedbackForm = this.fb.group({
      category: ['', Validators.required],
      ratingScale: ['', Validators.required],
      employee: [null],
      stakeholderEmail: ['', Validators.email],
    });
  }

  initRatingScaleForm() {
    this.ratingScaleForm = this.fb.group({
      scaletype: ['', Validators.required],
      ratingscales: ['', Validators.required],
    });
  }

  checkUserRole() {
    // Implement your logic to check if the user is an admin
    // This is just a placeholder, replace with your actual logic
    this.isAdmin = true; // or false, based on the user's role
  }

  showCreateRatingScaleDialog() {
    this.displayCreateRatingDialoge = true;
  }

  hideCreateRatingScaleDialog() {
    this.displayCreateRatingDialoge = false;
    this.ratingScaleForm.reset();
  }

  createRatingScale() {
    if (this.ratingScaleForm.valid) {
      const newRatingScale = {
        id: null,
        scaletype: this.ratingScaleForm.get('scaletype')?.value,
        ratingscales: this.ratingScaleForm.get('ratingscales')?.value,
      } as NewRatingScale;

      this.ratingScaleService.create(newRatingScale).subscribe(
        response => {
          console.log('Rating scale created successfully', response);
          this.loadRatingScales(); // Reload the rating scales
          this.hideCreateRatingScaleDialog();
        },
        error => {
          console.error('Error creating rating scale', error);
          // Handle error (e.g., show error message to user)
        },
      );
    }
  }
  loadRatingScales() {
    this.ratingScaleService.query().subscribe(
      res => {
        this.ratingScales = res.body ?? [];
      },
      error => console.error('Error loading rating scales:', error),
    );
  }

  getTheRequester() {
    this.feedbackRequestService.findByPin('6304').subscribe(
      res => {
        this.requester = res;
        this.checkExistingFeedback();
      },
      error => console.error('Error fetching requester:', error),
    );
  }

  checkExistingFeedback() {
    this.feedbackService.query(`requester.equals=${this.requester.id}&assessmentYear.equals=${this.currentYear}`).subscribe(
      res => {
        if (res.body && res.body.length > 0) {
          this.existingFeedback = res.body[0];
          this.feedbackForm.patchValue({
            ratingScale: this.existingFeedback.ratingScale?.scaletype,
          });
          this.loadExistingResponders(this.existingFeedback);
        }
      },
      error => console.error('Error fetching existing feedback:', error),
    );
  }

  loadExistingResponders(feedback: IFeedback) {
    this.feedbackRequestService.getrespondersByFeedbackId(feedback.id!).subscribe(
      res => {
        if (res && res.length > 0) {
          this.selectedResponders = res.map(r => ({
            category: r.category,
            employee: r.employee,
            stakeholderEmail: r.stakeholderEmail,
            status: r.responderStatus,
            requestDate: dayjs(feedback.requestDate).format('DD-MM-YYYY'),
          }));

          this.selfAndSupervisorAdded = this.selectedResponders.some(
            r => r.category === ResponderCategory.SELF || r.category === ResponderCategory.SUPERVISOR,
          );
          console.log('Loaded existing responders:', this.selectedResponders);
        }
      },
      error => console.error('Error loading existing responders:', error),
    );
  }

  searchEmployees(event: any) {
    const category = this.feedbackForm.get('category')?.value;
    const searchTerm = event.query;
    const ratingScale = this.feedbackForm.get('ratingScale')?.value;

    if (!category || !ratingScale || !searchTerm.trim()) {
      this.filteredEmployees = [];
      return;
    }

    this.isLoading = true;

    this.generateOrGetFeedback()
      .pipe(
        switchMap(() => this.feedbackRequestService.searchEmployees(searchTerm, category, '6304')),
        catchError(error => {
          console.error('Error generating feedback or searching employees:', error);
          this.isLoading = false;
          return of([]);
        }),
      )
      .subscribe(
        (employees: IEmployee[]) => {
          const selectedPins = this.selectedResponders.filter(r => r.employee).map(r => r.employee.pin);

          this.filteredEmployees = employees
            .filter(e => !selectedPins.includes(e.pin))
            .map(e => ({
              label: `${e.firstname} ${e.lastname} (${e.pin})`,
              value: e,
            }));
          this.isLoading = false;
        },
        error => {
          console.error('Error in searchEmployees:', error);
          this.isLoading = false;
        },
      );
  }

  selectEmployee(event: any) {
    const selectedEmployee = event.value;
    console.log('Selected employee in selectEmployee():', selectedEmployee);
    console.log('Selected employee in selectEmployee():', selectedEmployee.value!);
    this.feedbackForm.patchValue({
      employee: selectedEmployee,
    });
  }

  generateOrGetFeedback(): Observable<IFeedback> {
    if (this.existingFeedback) {
      return of(this.existingFeedback);
    } else {
      const newFeedback = this.feedbackFormService.transformToNewFeedback(this.feedbackForm, this.requester);
      return this.feedbackService.create(newFeedback).pipe(
        tap(res => {
          this.existingFeedback = res.body!;
        }),
        map(res => res.body!),
      );
    }
  }

  saveResponder() {
    const category = this.feedbackForm.get('category')?.value;

    if (!this.existingFeedback) {
      this.generateOrGetFeedback().subscribe(
        feedback => {
          this.existingFeedback = feedback;
          this.proceedWithSavingResponder(category);
        },
        error => console.error('Error generating feedback:', error),
      );
    } else {
      this.proceedWithSavingResponder(category);
    }
  }

  private proceedWithSavingResponder(category: ResponderCategory) {
    if (!this.selfAndSupervisorAdded) {
      this.addSelfAndSupervisor();
    }

    this.addNewResponder(category);

    this.feedbackForm.patchValue({ category: '', employee: null, stakeholderEmail: '' });
    this.filteredEmployees = [];
  }

  addSelfAndSupervisor() {
    // Add self
    this.addToResponderTable(this.existingFeedback!, ResponderCategory.SELF, this.requester, FeedbackStatus.NEW);
    // Add supervisor
    this.feedbackRequestService.getSupervisor('6304').subscribe(
      (supervisor: IEmployee) => {
        if (supervisor) {
          this.addToResponderTable(this.existingFeedback!, ResponderCategory.SUPERVISOR, supervisor, FeedbackStatus.NEW);
          this.selfAndSupervisorAdded = true;
        } else {
          console.warn('No supervisor found for the requester');
        }
      },
      error => console.error('Error fetching supervisor:', error),
    );
  }

  addNewResponder(category: ResponderCategory) {
    if (category === ResponderCategory.STAKEHOLDER) {
      const email = this.feedbackForm.get('stakeholderEmail')?.value;
      if (!email) {
        console.error('Stakeholder email is required');
        return;
      }
      this.addToResponderTable(this.existingFeedback!, category, null, FeedbackStatus.NEW, email);
    } else {
      const employee = this.feedbackForm.get('employee')?.value;
      console.log(this.feedbackForm.get('employee')?.value);
      console.log('Selected employee:', employee);
      if (!employee) {
        console.error('Selected employee not found');
        return;
      }
      this.addToResponderTable(this.existingFeedback!, category, employee.value!, FeedbackStatus.NEW);
    }
  }

  addToResponderTable(
    feedback: IFeedback,
    category: ResponderCategory,
    employee: IEmployee | null,
    status: FeedbackStatus,
    stakeholderEmail?: string,
  ) {
    const responderForm = this.fb.group({
      category: [category],
      stakeholderEmail: [stakeholderEmail],
      responderStatus: [status],
      employee: employee!,
      feedback: feedback, // Only send the feedback ID
    });
    console.log('responder Form', responderForm);
    const responder = this.responderFormService.getFeedbackResponder(responderForm as FormGroup) as NewFeedbackResponder;
    console.log(responder);
    this.responderService.create(responder).subscribe(
      res => {
        this.selectedResponders.push({
          category,
          employee: employee,
          stakeholderEmail,
          status,
          requestDate: dayjs().format('DD-MM-YYYY'),
        });
      },
      error => console.error('Error creating responder:', error),
    );
  }

  removeResponder(index: number) {
    const responderToRemove = this.selectedResponders[index];
    if (responderToRemove.id) {
      this.responderService.delete(responderToRemove.id).subscribe(
        () => {
          this.selectedResponders.splice(index, 1);
          if (this.selectedResponders.length === 0) {
            this.selfAndSupervisorAdded = false;
          }
        },
        error => console.error('Error deleting responder:', error),
      );
    } else {
      this.selectedResponders.splice(index, 1);
      if (this.selectedResponders.length === 0) {
        this.selfAndSupervisorAdded = false;
      }
    }
  }

  canSendToSupervisor(): boolean {
    const peerCount = this.selectedResponders.filter(r => r.category === ResponderCategory.PEER).length;
    const superviseeCount = this.selectedResponders.filter(r => r.category === ResponderCategory.SUPERVISEE).length;
    const newStatus = this.selectedResponders.some(r => r.status === FeedbackStatus.NEW);
    return peerCount > 0 && superviseeCount > 0 && newStatus;
  }

  sendToSupervisor() {
    if (this.canSendToSupervisor() && this.existingFeedback) {
      this.feedbackRequestService.sendToSupervisor(this.existingFeedback.id!).subscribe(
        () => {
          console.log('Feedback sent to supervisor');
          this.updateResponderStatuses();
        },
        error => console.error('Error sending feedback to supervisor:', error),
      );
    }
  }

  private updateResponderStatuses() {
    this.selectedResponders.forEach((responder, index) => {
      if (responder.status === FeedbackStatus.NEW) {
        const updatedResponder = {
          id: responder.id,
          category: responder.category,
          stakeholderEmail: responder.stakeholderEmail,
          responderStatus: FeedbackStatus.SENT_TO_SUPERVISOR,
          employee: responder.employee ? { id: responder.employee.id } : null,
          feedback: { id: this.existingFeedback!.id },
        };
        this.responderService.update(updatedResponder).subscribe(
          res => {
            this.selectedResponders[index] = {
              ...this.selectedResponders[index],
              status: FeedbackStatus.SENT_TO_SUPERVISOR,
            };
          },
          error => console.error('Error updating responder:', error),
        );
      }
    });
  }

  onCategoryChange() {
    this.feedbackForm.patchValue({
      employee: null,
      stakeholderEmail: '',
    });
    this.filteredEmployees = [];
  }

  isFormValid(): boolean {
    const category = this.feedbackForm.get('category')?.value;
    const ratingScale = this.feedbackForm.get('ratingScale')?.value;
    const employee = this.feedbackForm.get('employee')?.value;
    const stakeholderEmail = this.feedbackForm.get('stakeholderEmail')?.value;

    if (!category || !ratingScale) {
      return false;
    }

    if (category === ResponderCategory.STAKEHOLDER) {
      return !!stakeholderEmail && this.feedbackForm.get('stakeholderEmail')!.valid;
    } else {
      return !!employee;
    }
  }

  reset() {
    this.feedbackForm.reset();
    this.filteredEmployees = [];
  }
  checkRatingForm(): boolean {
    return this.ratingScaleForm.valid;
  }

  backToEHMS() {
    // Implement navigation logic to go back to eHMS
    console.log('Navigating back to eHMS');
    // You might want to use Angular Router to navigate
    // this.router.navigate(['/ehms']);
  }

  // Helper method for PrimeNG dropdown
  compareRatingScale = (o1: IRatingScale | null, o2: IRatingScale | null): boolean => this.ratingScaleService.compareRatingScale(o1, o2);
}
