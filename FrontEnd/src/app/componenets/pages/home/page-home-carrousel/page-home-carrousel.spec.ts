import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageHomeCarrousel } from './page-home-carrousel';

describe('PageHomeCarrousel', () => {
  let component: PageHomeCarrousel;
  let fixture: ComponentFixture<PageHomeCarrousel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageHomeCarrousel]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageHomeCarrousel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
