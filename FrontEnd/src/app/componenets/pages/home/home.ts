import { Component } from '@angular/core';
import { PageHomeBanner } from './page-home-banner/page-home-banner';
import { PageHomeCarrousel } from './page-home-carrousel/page-home-carrousel';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

@Component({
  selector: 'app-home',
  imports: [
    Header,
    Footer,
    PageHomeBanner, 
    PageHomeCarrousel
  ],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
