import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedInformationComponent } from './need-information.component';

describe('NeedInformationComponent', () => {
  let component: NeedInformationComponent;
  let fixture: ComponentFixture<NeedInformationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NeedInformationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NeedInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
