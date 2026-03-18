import { Component } from '@angular/core';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';

@Component({
  selector: 'app-page-faq',
  imports: [
    Header, 
    Footer
  ],
  templateUrl: './page-faq.html',
  styleUrl: './page-faq.css',
})
export class PageFaq {

}
