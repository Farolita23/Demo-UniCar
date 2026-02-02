import { Component } from '@angular/core';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';

@Component({
    selector: 'app-page-profile',
    imports: [
        Header,
        Footer,
    ],
    templateUrl: './page-profile.html',
    styleUrl: './page-profile.css',
})
export class PageProfile {

}
