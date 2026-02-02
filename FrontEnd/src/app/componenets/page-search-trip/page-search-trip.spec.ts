import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageSearchTrip } from './page-search-trip';

describe('PageSearchTrip', () => {
  let component: PageSearchTrip;
  let fixture: ComponentFixture<PageSearchTrip>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageSearchTrip]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageSearchTrip);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
