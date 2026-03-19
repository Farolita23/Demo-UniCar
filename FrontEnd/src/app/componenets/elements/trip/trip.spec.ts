import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Trip } from './trip';

describe('Trip', () => {
  let component: Trip;
  let fixture: ComponentFixture<Trip>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Trip]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Trip);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
