import { Component, Input } from '@angular/core';
import { Rating } from '../rating/rating';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AppConfig } from '../../../services/app-config';

@Component({
  selector: 'app-trip',
  imports: [
    RouterLink,
    RouterLinkActive,
    Rating,
  ],
  templateUrl: './trip.html',
  styleUrl: './trip.css',
})
export class Trip {
    @Input() data: any;
    constructor(
        public config: AppConfig
    ){}
}
