import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedbackSuperviseeListComponent } from './feedback-supervisee-list.component';

describe('FeedbackSuperviseeListComponent', () => {
  let component: FeedbackSuperviseeListComponent;
  let fixture: ComponentFixture<FeedbackSuperviseeListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FeedbackSuperviseeListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FeedbackSuperviseeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
