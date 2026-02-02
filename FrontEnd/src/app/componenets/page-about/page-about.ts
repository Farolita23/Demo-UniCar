import { Component } from '@angular/core';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';

@Component({
    selector: 'app-page-about',
    imports: [
        Header,
        Footer
    ],
    templateUrl: './page-about.html',
    styleUrl: './page-about.css',
    
})
export class PageAbout {}
