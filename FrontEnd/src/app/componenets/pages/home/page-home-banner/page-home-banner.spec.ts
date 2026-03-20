import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageHomeBanner } from './page-home-banner';

describe('PageHomeBanner', () => {
  let component: PageHomeBanner;
  let fixture: ComponentFixture<PageHomeBanner>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageHomeBanner]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageHomeBanner);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
