import { Component } from '@angular/core';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';

@Component({
  selector: 'app-faq',
  imports: [
    Header, 
    Footer
  ],
  templateUrl: './faq.html',
  styleUrl: './faq.css',
})
export class Faq {}
