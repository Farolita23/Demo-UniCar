import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageAbout } from './page-about';

describe('PageAbout', () => {
  let component: PageAbout;
  let fixture: ComponentFixture<PageAbout>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageAbout]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageAbout);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
