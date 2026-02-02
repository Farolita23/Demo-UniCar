import { Component } from '@angular/core';
import { PageHomeBanner } from './page-home-banner/page-home-banner';
import { PageHomeCard } from './page-home-card/page-home-card';
import { PageHomeCarrousel } from './page-home-carrousel/page-home-carrousel';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';

@Component({
  selector: 'app-page-home',
  imports: [
    Header,
    Footer,
    PageHomeBanner, 
    PageHomeCard, 
    PageHomeCarrousel
  ],
  templateUrl: './page-home.html',
  styleUrl: './page-home.css',
})
export class PageHome {}
