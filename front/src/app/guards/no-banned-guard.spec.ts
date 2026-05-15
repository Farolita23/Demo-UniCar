import { TestBed } from '@angular/core/testing';

import { NoBannedGuard } from '../services/no-banned-guard';

describe('NoBannedGuard', () => {
  let service: NoBannedGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(NoBannedGuard);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
