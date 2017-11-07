import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamHeadComponent } from './team-head.component';

describe('TeamHeadComponent', () => {
  let component: TeamHeadComponent;
  let fixture: ComponentFixture<TeamHeadComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TeamHeadComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamHeadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
