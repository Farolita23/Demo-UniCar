import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchTrip } from './search-trip';

describe('PageSearchTrip', () => {
    let component: SearchTrip;
    let fixture: ComponentFixture<SearchTrip>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [SearchTrip]
        })
            .compileComponents();

        fixture = TestBed.createComponent(SearchTrip);
        component = fixture.componentInstance;
        await fixture.whenStable();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
