import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageSignup } from './page-signup';

describe('PageSignup', () => {
  let component: PageSignup;
  let fixture: ComponentFixture<PageSignup>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageSignup]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageSignup);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
