import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecoveryPassword } from './recovery-password';

describe('RecoveryPassword', () => {
  let component: RecoveryPassword;
  let fixture: ComponentFixture<RecoveryPassword>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecoveryPassword]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecoveryPassword);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
