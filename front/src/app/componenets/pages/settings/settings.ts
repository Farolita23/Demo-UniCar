import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Header } from '../../elements/header/header';
import { Footer } from '../../elements/footer/footer';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'page-settings',
  standalone: true,
  imports: [CommonModule, Header, Footer],
  templateUrl: './settings.html',
  styleUrl: './settings.css',
})
export class Settings {
  auth = inject(AuthService);
  router = inject(Router);
  confirmLogout = false;

  logout() {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
