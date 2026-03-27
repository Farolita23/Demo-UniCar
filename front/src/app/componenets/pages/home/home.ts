import { Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { Trip } from '../../../models/trip.model';

@Component({
  selector: 'page-home',
  standalone: true,
  imports: [RouterLink, CommonModule, Header, Footer, TripCard, AppIcon],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  api = inject(ApiService);
  platformId = inject(PLATFORM_ID);
  recentTrips: Trip[] = [];

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.api.getTrips(0, 6).subscribe({ next: (p) => this.recentTrips = p.content, error: () => {} });
    }
  }

  updateTrip(updated: Trip, index: number) {
    this.recentTrips[index] = updated;
  }

  stats = [
    { value: '500+', label: 'Usuarios activos' },
    { value: '1200+', label: 'Viajes realizados' },
    { value: '25+', label: 'Campus universitarios' },
    { value: '4.8★', label: 'Valoración media' },
  ];
}
