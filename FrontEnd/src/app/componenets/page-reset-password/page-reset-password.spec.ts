import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageResetPassword } from './page-reset-password';

describe('PageResetPassword', () => {
  let component: PageResetPassword;
  let fixture: ComponentFixture<PageResetPassword>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageResetPassword]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageResetPassword);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
