import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageRecoveryPassword } from './page-recovery-password';

describe('PageRecoveryPassword', () => {
  let component: PageRecoveryPassword;
  let fixture: ComponentFixture<PageRecoveryPassword>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageRecoveryPassword]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageRecoveryPassword);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
