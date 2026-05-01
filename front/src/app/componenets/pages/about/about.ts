import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AppIcon } from '../../elements/icon/icon';

@Component({
    selector: 'page-about',
    standalone: true,
    imports: [CommonModule, Header, Footer, AppIcon],
    templateUrl: './about.html',
    styleUrl: './about.css',
})
export class About { }
