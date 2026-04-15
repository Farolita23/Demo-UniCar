import { Component, inject, HostListener } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AppConfig } from '../../../services/app-config';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  config = inject(AppConfig);
  auth = inject(AuthService);
  showNav = false;
  showDropdown = false;

  alternateNav() { this.showNav = !this.showNav; }
  toggleDropdown() { this.showDropdown = !this.showDropdown; }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    const target = event.target as HTMLElement;
    if (!target.closest('.profile-dropdown-wrap')) {
      this.showDropdown = false;
    }
  }
}
