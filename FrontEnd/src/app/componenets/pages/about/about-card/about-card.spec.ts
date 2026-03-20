import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AboutCard } from './about-card';

describe('PageHomeCard', () => {
  let component: AboutCard;
  let fixture: ComponentFixture<AboutCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AboutCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AboutCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
