import { Component } from '@angular/core';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

@Component({
    selector: 'pages-trip',
    imports: [
        Header,
        Footer,
    ],
    templateUrl: './trip.html',
    styleUrl: './trip.css',
})

export class PageTrip {}