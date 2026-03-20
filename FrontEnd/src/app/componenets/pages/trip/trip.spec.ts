import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageTrip } from './trip';

describe('Trip', () => {
  let component: PageTrip;
  let fixture: ComponentFixture<PageTrip>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageTrip]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageTrip);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
