import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageLogout } from './page-logout';

describe('PageLogout', () => {
  let component: PageLogout;
  let fixture: ComponentFixture<PageLogout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageLogout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageLogout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
