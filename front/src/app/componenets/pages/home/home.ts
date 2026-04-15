import { Component, inject, OnInit, ChangeDetectorRef, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { TripCard } from '../../elements/trip/trip';
import { AppIcon } from '../../elements/icon/icon';
import { ApiService } from '../../../services/api-service';
import { AuthService } from '../../../services/auth-service';
import { Trip } from '../../../models/trip.model';

@Component({
  selector: 'page-home',
  standalone: true,
  imports: [RouterLink, CommonModule, Header, Footer, TripCard, AppIcon],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  api        = inject(ApiService);
  auth       = inject(AuthService);
  cdr        = inject(ChangeDetectorRef);
  platformId = inject(PLATFORM_ID);

  suggestedTrips: Trip[] = [];
  loadingTrips = true;
  isSuggested  = false; // true = personalized, false = general future

  features = [
    { icon: '🔍', title: 'Busca viajes', desc: 'Filtra por campus, localidad, fecha y dirección para encontrar el trayecto perfecto.' },
    { icon: '🚗', title: 'Publica el tuyo', desc: 'Ofrece plazas libres en tu coche, fija un precio y gestiona quién viaja contigo.' },
    { icon: '✅', title: 'Confirma y viaja', desc: 'Solicita plaza, el conductor acepta y compartes el camino. Así de fácil.' },
  ];

  stats = [
    { value: '500+',  label: 'Usuarios activos', icon: '👥' },
    { value: '1200+', label: 'Viajes realizados', icon: '🛣' },
    { value: '25+',   label: 'Campus conectados', icon: '🎓' },
    { value: '4.8★',  label: 'Valoración media',  icon: '⭐' },
  ];

  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) { this.loadingTrips = false; return; }

    const user = this.auth.getUser();
    if (user?.id) {
      // Logged in: get personalized suggestions
      this.isSuggested = true;
      this.api.getSuggestedTrips(user.id, 0, 6).subscribe({
        next: p => {
          this.suggestedTrips = p.content;
          this.isSuggested = p.content.length > 0;
          this.loadingTrips = false;
          this.cdr.detectChanges();
        },
        error: () => { this.loadFutureTrips(); },
      });
    } else {
      this.loadFutureTrips();
    }
  }

  private loadFutureTrips() {
    this.isSuggested = false;
    this.api.getFutureTrips(0, 6).subscribe({
      next: p => { this.suggestedTrips = p.content; this.loadingTrips = false; this.cdr.detectChanges(); },
      error: () => { this.loadingTrips = false; this.cdr.detectChanges(); },
    });
  }

  updateTrip(updated: Trip, index: number) {
    this.suggestedTrips[index] = updated;
    this.cdr.detectChanges();
  }

  get userName(): string {
    return this.auth.getUser()?.name?.split(' ')[0] || '';
  }
}
