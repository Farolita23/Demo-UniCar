import { Component } from '@angular/core';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

@Component({
    selector: 'app-profile',
    imports: [
        Header,
        Footer,
    ],
    templateUrl: './profile.html',
    styleUrl: './profile.css',
})
export class Profile {}
