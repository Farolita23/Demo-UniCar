import { Component } from '@angular/core';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';

@Component({
    selector: 'app-page-search-trip',
    imports: [
        Header,
        Footer
    ],
    templateUrl: './page-search-trip.html',
    styleUrl: './page-search-trip.css',
})
export class PageSearchTrip {

}
