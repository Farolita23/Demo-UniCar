import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageProfile } from './page-profile';

describe('PageProfile', () => {
  let component: PageProfile;
  let fixture: ComponentFixture<PageProfile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageProfile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageProfile);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
