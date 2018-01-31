import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestResourceComponent } from './request-resource.component';

describe('RequestResourceComponent', () => {
  let component: RequestResourceComponent;
  let fixture: ComponentFixture<RequestResourceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RequestResourceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestResourceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
