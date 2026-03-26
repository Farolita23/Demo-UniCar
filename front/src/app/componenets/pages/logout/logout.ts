import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'page-logout',
  standalone: true,
  imports: [],
  template: '',
})
export class Logout implements OnInit {
  auth = inject(AuthService);
  router = inject(Router);
  ngOnInit() { this.auth.logout(); this.router.navigate(['/']); }
}
