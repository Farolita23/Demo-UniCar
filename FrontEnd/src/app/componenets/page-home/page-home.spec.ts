import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageHome } from './page-home';

describe('PageHome', () => {
  let component: PageHome;
  let fixture: ComponentFixture<PageHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageHome);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
