import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageHomeCard } from './page-home-card';

describe('PageHomeCard', () => {
  let component: PageHomeCard;
  let fixture: ComponentFixture<PageHomeCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PageHomeCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PageHomeCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
