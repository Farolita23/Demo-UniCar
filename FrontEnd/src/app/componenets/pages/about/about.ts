import { Component } from '@angular/core';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AboutCard } from './about-card/about-card';

@Component({
    selector: 'app-about',
    imports: [
        Header,
        AboutCard,
        Footer
    ],
    templateUrl: './about.html',
    styleUrl: './about.css',
})
export class About {}
